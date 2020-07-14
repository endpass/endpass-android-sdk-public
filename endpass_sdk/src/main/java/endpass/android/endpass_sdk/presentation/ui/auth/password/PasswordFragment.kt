package endpass.android.endpass_sdk.presentation.ui.auth.password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.impl.PasswordStrategy
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import kotlinx.android.synthetic.main.fragment_password.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.sharedViewModel


class PasswordFragment : Fragment() {


    companion object {
        fun newInstance(): PasswordFragment {
            return PasswordFragment()
        }
    }

    private val mViewModel: AuthViewModel by sharedViewModel(from = { parentFragment!! })
    private val router by inject<MainRouter>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setListeners()
    }

    private fun setListeners() {
        changePassTv.setOnClickListener {
            router.changePassword(context, mViewModel.loginExtraData)
        }
    }

    private fun initView() {
        passInputLayout.setValidationStrategy(PasswordStrategy()) { isValidate ->
            val loginExtraData = loginExtraData()
            if (isValidate) {
                loginExtraData.password = getPassFromInput()
            } else {
                loginExtraData.password = null
            }
        }
    }

    private fun loginExtraData() = mViewModel.loginExtraData


    private fun getPassFromInput() = passInputLayout.getValue()


}