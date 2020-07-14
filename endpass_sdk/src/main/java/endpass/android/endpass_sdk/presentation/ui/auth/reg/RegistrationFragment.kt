package endpass.android.endpass_sdk.presentation.ui.auth.reg

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.impl.CodeStrategy
import endpass.android.endpass_sdk.presentation.impl.EmailStrategy
import endpass.android.endpass_sdk.presentation.impl.PasswordStrategy
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.ui.auth.verify.VerificationFragment
import endpass.android.endpass_sdk.presentation.vo.Status
import endpass.android.endpass_sdk.gateway.EnumCollections
import kotlinx.android.synthetic.main.fragment_registration.*
import org.koin.android.viewmodel.ext.android.viewModel


class RegistrationFragment : Fragment() {


    companion object {
        fun newInstance(): RegistrationFragment {
            return RegistrationFragment()
        }
    }

    private val mViewModel: AuthViewModel by viewModel()
    private var scenario = EnumCollections.LoginScenario.LOGIN

    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_registration, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeLiveData()
        setListeners()
        includeVerifyFragment()
    }


    private fun initView() {
       // loginInputLayout.setValue("dev+meirlen1990@endpass.com")
        loginInputLayout.setValidationStrategy(EmailStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        newPassInputLayout.setValidationStrategy(PasswordStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        confirmPassInputLayout.setValidationStrategy(PasswordStrategy()) { signUpBtn.setDisable(!isAllDataCorrectFilled()) }
        receivedInputLayout.setValidationStrategy(CodeStrategy())
    }

    private fun getPassFromInput() = confirmPassInputLayout.getValue()

    private fun getEmailFromInput() = loginInputLayout.getValue()


    private fun loginExtraData() = mViewModel.loginExtraData


    private fun observeLiveData() {
        mViewModel.loginLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> successHandler(it.data)
                Status.ERROR -> toast(getString(R.string.error_text))
            }
        })

    }

    private fun successHandler(scenario: EnumCollections.LoginScenario?) {
        if (scenario != null) {
            this.scenario = scenario
            if (isRegScenario()) {
                mViewModel.requestCode(getEmailFromInput())
                expandableItem(true)
                signUpBtn.hide()
            }

            loginExtraData().email = getEmailFromInput()
            loginExtraData().password = getPassFromInput()
        }
        hideProgress()

    }

    private fun includeVerifyFragment() {
        childFragmentManager.replaceOnce(
            R.id.verify_frame_container,
            VerificationFragment::javaClass.name,
            {
                val fragment =
                    VerificationFragment.newInstance(
                        EnumCollections.LoginScenario.REGISTRATION.screen
                    )
                fragment
            }
        ).commit()
    }


    private fun isRegScenario() =
        scenario == EnumCollections.LoginScenario.PASSWORD || scenario == EnumCollections.LoginScenario.REGISTRATION


    private fun hideProgress() {
        progressBar.hide()
        signUpBtn.text = context?.getString(R.string.sign_up)
    }

    private fun showProgress() {
        //if (signUpBtn.isVisible) {
            progressBar.show()
            signUpBtn.clearText()
       // }
    }

    private fun setListeners() {
        signUpBtn.setOnClickListener {
            if (isEqualTwoPass()) {
                if (isAllDataCorrectFilled()) {
                    rootView.hideKeyboard()
                    mViewModel.login(getEmailFromInput())
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
        loginInputLayout.isValidate() && newPassInputLayout.isValidate() &&
                newPassInputLayout.getValue() == confirmPassInputLayout.getValue()

}