package endpass.android.endpass_sdk.presentation.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.impl.CodeStrategy
import endpass.android.endpass_sdk.presentation.impl.PasswordStrategy
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.ui.auth.verify.VerificationFragment
import endpass.android.endpass_sdk.presentation.vo.Status
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.login.LoginExtraData
import kotlinx.android.synthetic.main.fragment_change_pass.*
import org.koin.android.viewmodel.ext.android.viewModel


class ChangePassFragment : Fragment() {

    companion object {

        private const val BUNDLE_LOGIN_DATA = "scenario_bundle"

        fun newInstance(loginExtraData: LoginExtraData): ChangePassFragment {
            return ChangePassFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_LOGIN_DATA, loginExtraData)
                }
            }
        }

    }


    private val mViewModel: AuthViewModel by viewModel()
    private var scenario = EnumCollections.LoginScenario.CHANGE_PASS
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_change_pass, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setListeners()
        includeVerifyFragment()
        if (!isOtpCode()) {
            requestCode()
        }
    }


    private fun initView() {
        receivedInputLayout.setValidationStrategy(CodeStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        newPassInputLayout.setValidationStrategy(PasswordStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        confirmPassInputLayout.setValidationStrategy(PasswordStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        receivedInputLayout.setValidationStrategy(CodeStrategy())
        mViewModel.loginExtraData = getLoginDataFromBundle()!!

        if (isOtpCode()) {
            expandableItem(true)
            rootView.hideGroupViews(
                receivedInputLayout,
                newPassInputLayout,
                confirmPassInputLayout,
                signUpBtn
            )
        }

    }

    private fun isOtpCode() =
        loginExtraData().challengeType == EnumCollections.ChallengeType.OTP.key

    private fun getLoginDataFromBundle() =
        arguments?.getParcelable<LoginExtraData>(BUNDLE_LOGIN_DATA)

    private fun requestCode() = mViewModel.requestCode(loginExtraData().email)

    private fun getPassFromInput() = confirmPassInputLayout.getValue()

    private fun getCodeFromInput() = receivedInputLayout.getValue()

    private fun loginExtraData() = mViewModel.loginExtraData


    private fun observeLiveData() {
        mViewModel.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> successHandler(it.data)
                Status.ERROR -> showError()
            }
        })

    }

    private fun successHandler(scenario: EnumCollections.LoginScenario?) {
        if (scenario != null) {
            if (!isOtpCode()) {
                loginExtraData().password = getPassFromInput()
            }

            if (scenario == EnumCollections.LoginScenario.CHANGE_PASS) {
                requestCode()
                expandableItem(true)
                signUpBtn.hide()
                this.scenario = EnumCollections.LoginScenario.RESENT_CODE
            }
        }
        hideProgress()

    }

    private fun includeVerifyFragment() {
        scenario = when {
            loginExtraData().scenario != null -> EnumCollections.LoginScenario.RECOVER_CONFIRM
            isOtpCode() -> EnumCollections.LoginScenario.RECOVER
            else -> EnumCollections.LoginScenario.CHANGE_PASS
        }

        if (scenario == EnumCollections.LoginScenario.RECOVER_CONFIRM) {
            requestCode()
        }


        childFragmentManager.replaceOnce(
            R.id.verify_frame_container,
            VerificationFragment::javaClass.name,
            {
                val fragment = VerificationFragment.newInstance(scenario.screen)
                fragment
            }
        ).commit()
    }

    private fun hideProgress() {
        progressBar.hide()
        signUpBtn.text = context?.getString(R.string.sign_up)
    }

    private fun showProgress() {
        if (scenario == EnumCollections.LoginScenario.CHANGE_PASS) {
            progressBar.show()
            signUpBtn.clearText()
        }
    }

    private fun showError() {

    }

    private fun setListeners() {
        signUpBtn.setOnClickListener {
            if (isEqualTwoPass()) {
                if (isAllDataCorrectFilled()) {
                    rootView.hideKeyboard()
                    mViewModel.changePassword(getCodeFromInput(), getPassFromInput())
                }
            } else {
                toast(getString(R.string.pass_dont_match))
            }

        }
    }

    private fun expandableItem(isChecked: Boolean) =
        if (isChecked) expandableView.expand() else expandableView.collapse()


    private fun isEqualTwoPass() =
        newPassInputLayout.getValue() == confirmPassInputLayout.getValue()

    private fun isAllDataCorrectFilled() =
        receivedInputLayout.isValidate() && newPassInputLayout.isValidate() &&
                newPassInputLayout.getValue() == confirmPassInputLayout.getValue()

}