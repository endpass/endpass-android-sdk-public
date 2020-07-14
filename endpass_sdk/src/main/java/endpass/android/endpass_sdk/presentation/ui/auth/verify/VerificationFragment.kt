package endpass.android.endpass_sdk.presentation.ui.auth.verify

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant.OTP_FIELDS_COUNT
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.impl.*
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.HomeActivity.Companion.HOME
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.utils.Encryption
import endpass.android.endpass_sdk.presentation.utils.LocalData
import endpass.android.endpass_sdk.presentation.vo.Status
import endpass.android.endpass_sdk.gateway.EnumCollections.*
import endpass.android.endpass_sdk.presentation.ui.auth.AuthActivity.Companion.EXTRA_FLOW_TYPE
import endpass.android.endpass_sdk.presentation.ui.auth.OAuthViewModel
import endpass.android.endpass_sdk.presentation.utils.AppSingleton
import kotlinx.android.synthetic.main.fragment_verify.*
import kotlinx.android.synthetic.main.fragment_verify.mProgressBar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class VerificationFragment : DialogFragment() {


    companion object {

        const val LOGIN_STRATEGY_TYPE_BUNDLE = "scenario_bundle"

        fun newInstance(scenario: Int? = null): VerificationFragment {
            return VerificationFragment().apply {
                arguments = Bundle().apply {
                    putInt(LOGIN_STRATEGY_TYPE_BUNDLE, scenario ?: LoginScenario.LOGIN.screen)
                }
            }
        }
    }

    private val localData by inject<LocalData>()
    private val router by inject<MainRouter>()
    private val mViewModel: AuthViewModel by sharedViewModel(from = { parentFragment!! })
    private val oauthViewModel: OAuthViewModel by sharedViewModel(from = { parentFragment!! })
    private var verifyCode: String? = null
    private lateinit var rootView: View
    private lateinit var strategy: VerifyFragmentStrategy
    private lateinit var callback: () -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_verify, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setListeners()
        setUpStrategy()
    }

    fun setStateListener(callback: () -> Unit) {
        this.callback = callback
    }

    private fun getFlowType() = activity?.intent?.getIntExtra(EXTRA_FLOW_TYPE, 0)

    private fun getScenarioTypeBundle() = arguments?.getInt(LOGIN_STRATEGY_TYPE_BUNDLE) ?: 0

    private fun setUpStrategy() {
        strategy = createStrategy(mViewModel.loginExtraData.challengeType, getScenarioTypeBundle())
        initView()
    }

    private fun initView() {
        descTv.text = strategy.desc()
        resentTv.text = strategy.resentDesc()
        separateLine.setVisibility(strategy.isLineVisibilty())
    }

    private fun loginExtraData() = mViewModel.loginExtraData

    private fun observeLiveData() {
        mViewModel.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> successHandler(it.data)
                Status.ERROR -> showError()
            }
        })

        oauthViewModel.oauthFlowLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> hideProgress()
                Status.ERROR -> showError()
            }
        })

    }

    private fun showError() {
        showSnackbar(rootView, strategy.errorMsg(), MessageType.ERROR)
        hideProgress()
    }

    private fun successHandler(scenario: LoginScenario?) {
        hideProgress()
        if (scenario != null) {
            when (scenario) {
                LoginScenario.EMAIL_CONFIRMED -> {
                    if (mViewModel.token != null) {
                        localData.accessToken = Encryption.encryptData(mViewModel.token)
                        localData.email = loginExtraData().email
                        if (getScenarioTypeBundle() == LoginScenario.REGISTRATION.screen) {
                            router.openHome(context, HOME, loginExtraData())
                        } else {
                            if (getFlowType() == AuthFlowType.OAUTH.ordinal) {
                                activity?.setResult(Activity.RESULT_OK)
                                activity?.finish()
                            } else {
                                router.openHome(context, HOME, loginExtraData())

                            }

                        }
                    }
                }
                LoginScenario.RESENT_CODE -> showSnackbar(rootView, getString(R.string.code_sended))
                LoginScenario.RECOVER -> router.changePassword(context, mViewModel.loginExtraData)
                LoginScenario.RECOVER_CONFIRM -> router.changePassword(
                    context,
                    mViewModel.loginExtraData
                )
                LoginScenario.REMOVE_DOCUMENT -> {
                    dismiss()
                    callback.invoke()
                }

                else -> {
                    showSnackbar(rootView, getString(R.string.code_sended))
                    setUpStrategy()
                }
            }
        }
    }


    private fun hideProgress() {
        mProgressBar.hide()
        confirmBtn.text = context?.getString(R.string.confirm)
    }

    private fun showProgress() {
        mProgressBar.show()
        confirmBtn.clearText()
    }

    private fun isOtpCode() = loginExtraData().challengeType == ChallengeType.OTP.key

    private fun isEmailAndPassFill() =
        loginExtraData().password != null && loginExtraData().email != null

    private fun setListeners() {

        otpView.setOtpCompletionListener {
            confirmBtn.setDisable(it.length != OTP_FIELDS_COUNT)
            verifyCode = if (it.length == OTP_FIELDS_COUNT) {
                it
            } else {
                null
            }
        }
        confirmBtn.setOnClickListener {

            if (verifyCode != null) {
                val loginExtraData = loginExtraData()


                if (strategy is RemoveDocumentStrategy) {
                    mViewModel.deleteDocument(loginExtraData.documentId!!, verifyCode!!)
                } else if (strategy is OauthStrategy) {
                    showProgress()
                    oauthViewModel.oauthLogin(AppSingleton.challenge?.trim(), verifyCode)
                } else {
                    if (isEmailAndPassFill()) {
                        rootView.hideKeyboard()
                        loginExtraData.code = verifyCode
                        when (getScenarioTypeBundle()) {
                            LoginScenario.REGISTRATION.screen -> mViewModel.signUp(loginExtraData)
                            LoginScenario.RECOVER.screen -> mViewModel.confirmRecover(
                                loginExtraData.email!!, loginExtraData.recoverCode!!
                            )
                            else -> mViewModel.verifyEmail(loginExtraData)
                        }

                    } else {
                        toast(getString(R.string.enter_your_password))
                    }
                }
            } else {
                toast(getString(R.string.enter_your_code))
            }
        }

        resentTv.setOnClickListener {
            if (isOtpCode()) {
                if (isEmailAndPassFill()) {
                    mViewModel.recover(loginExtraData().email!!)
                } else {
                    showSnackbar(rootView, getString(R.string.enter_your_password))
                }

            } else {
                mViewModel.requestCode(loginExtraData().email, LoginScenario.RESENT_CODE)

            }
        }

    }


    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        if (::callback.isInitialized) {
            callback.invoke()
        }
    }

}