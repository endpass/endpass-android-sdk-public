package endpass.android.endpass_sdk.presentation.ui.auth.login

import androidx.lifecycle.Observer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_login.*
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.vo.Status
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.impl.EmailStrategy
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.ui.auth.password.PasswordFragment
import endpass.android.endpass_sdk.presentation.ui.auth.reg.RegistrationFragment
import endpass.android.endpass_sdk.presentation.ui.auth.verify.VerificationFragment
import endpass.android.endpass_sdk.presentation.utils.NetworkHandler
import endpass.android.endpass_sdk.gateway.EnumCollections.*
import kotlinx.android.synthetic.main.fragment_login.loginBtn
import kotlinx.android.synthetic.main.fragment_login.mProgressBar
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginFragment : androidx.fragment.app.Fragment() {


    companion object {

        const val LOGIN_STRATEGY_TYPE_BUNDLE = "scenario_bundle"

        fun newInstance(scenario: Int): LoginFragment {
            return LoginFragment().apply {
                arguments = Bundle().apply {
                    putInt(LOGIN_STRATEGY_TYPE_BUNDLE, scenario)
                }
            }
        }
    }

    private val mViewModel: AuthViewModel by viewModel()
    private val networkHandler by inject<NetworkHandler>()
    private var scenario = LoginScenario.LOGIN
    private lateinit var rootView: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_login, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setListeners()
        initContentBody()

    }

    private fun initContentBody() {
        includeVerifyFragment()
        if (getScenarioTypeBundle() == LoginScenario.LOGIN.screen) {
            includeLoginFieldsFragment()
        } else {
            includeRegisterFieldsFragment()
        }
    }

    private fun getScenarioTypeBundle() = arguments?.getInt(LOGIN_STRATEGY_TYPE_BUNDLE) ?: 0

    private fun initView() {
        loginInputLayout.setValidationStrategy(EmailStrategy())
        loginInputLayout.focus()
        loginInputLayout.setValidationStrategy(EmailStrategy()) { isValidate ->
            loginBtn.setDisable(!isValidate)
            resetPassParams()
        }
        loginInputLayout.setValue("dev+miko1991@endpass")

    }

    private fun resetPassParams() {
        if (scenario != LoginScenario.LOGIN) {
            expandableItem(false)
            loginBtn.show()
        }

    }

    private fun login(email: String) {
        if (!networkHandler.isConnected) {
            return
        }
        mViewModel.login(email)
    }

    private fun extraLoginData() = mViewModel.loginExtraData

    private fun getEmailFromInput() = loginInputLayout.getValue()


    /**
     * Подписка на LiveData
     * @see AuthViewModel
     */

    private fun observeLiveData() {
        mViewModel.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> successHandler(it.data)
                Status.ERROR -> errorHandler(it.error)
            }
        })

    }

    private fun includeLoginFieldsFragment() {
        childFragmentManager.replaceOnce(
            R.id.fields_frame_container,
            PasswordFragment::javaClass.name,
            {
                val fragment = PasswordFragment.newInstance()
                fragment
            }
        ).commit()
    }

    private fun includeRegisterFieldsFragment() {
        childFragmentManager.replaceOnce(
            R.id.fields_frame_container,
            RegistrationFragment::javaClass.name,
            {
                val fragment =
                    RegistrationFragment.newInstance()
                fragment
            }
        ).commit()
    }

    private fun includeVerifyFragment() {
        childFragmentManager.replaceOnce(
            R.id.verify_frame_container,
            VerificationFragment::javaClass.name,
            {
                val fragment =
                    VerificationFragment.newInstance()
                fragment
            }
        ).commit()
    }

    private fun errorHandler(throwable: Throwable?) {
        if (throwable != null) {
            //    showSnackbar(rootView, throwable.localizedMessage)
        }
        hideProgress()
    }

    private fun successHandler(scenario: LoginScenario?) {
        this.scenario = scenario!!
        hideProgress()
        if (scenario == LoginScenario.PASSWORD) {
            expandableItem(true)
            mViewModel.requestCode(extraLoginData().email!!)
            loginBtn.hide()
        }
        if (scenario == LoginScenario.REGISTRATION) {
            showSnackbar(rootView, getString(R.string.dont_have_account),MessageType.ERROR)
        }


    }

    private fun hideProgress() {
        mProgressBar.hide()
        loginBtn.text = context?.getString(R.string.login)

    }

    private fun showProgress() {
        mProgressBar.show()
        loginBtn.clearText()
    }

    private fun expandableItem(isChecked: Boolean) =
        if (isChecked) expandableView.expand() else expandableView.collapse()

    private fun setListeners() {
        loginBtn.setOnClickListener {
            if (loginInputLayout.isValidate()) {
                rootView.hideKeyboard()
                login(getEmailFromInput())
                extraLoginData().email = getEmailFromInput()
            }
        }
    }

}