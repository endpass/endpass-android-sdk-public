package endpass.android.endpass_sdk.presentation.ui.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.auth.verify.VerificationFragment
import endpass.android.endpass_sdk.presentation.vo.Status
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import endpass.android.endpass_sdk.presentation.ui.dialog.ScopeAccessFragment
import endpass.android.endpass_sdk.presentation.ui.result.EndpassManager
import endpass.android.endpass_sdk.presentation.utils.LocalData
import endpass.android.endpass_sdk.presentation.utils.PKCE
import kotlinx.android.synthetic.main.fragment_oauth.*


class OauthFragment : androidx.fragment.app.Fragment() {

    private val router by inject<MainRouter>()

    private val oAuthViewModel: OAuthViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var verificationFragment: VerificationFragment
    private lateinit var scopeAccessFragment: ScopeAccessFragment
    private val localData by inject<LocalData>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_oauth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        login()
        initView()
    }

    private fun initView() {
        PKCE.createCodeVerifier()
        observeLiveData()
    }

    private fun login() {

        if (localData.accessToken.isEmpty()) {
            router.openLogin(this, EnumCollections.AuthFlowType.OAUTH.ordinal)
        } else {
            oAuthViewModel.oauth()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            oAuthViewModel.oauth()
        }

    }


    private fun observeLiveData() {

        oAuthViewModel.oauthFlowLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    localData.oauthToken = it.data?.access_token
                    if (EndpassManager.fragment != null) {
                        EndpassManager.request(
                            EndpassManager.requestType,
                            EndpassManager.fragment!!
                        )
                    } else {
                        EndpassManager.request(
                            EndpassManager.requestType,
                            EndpassManager.activity!!
                        )

                    }
                    // activity?.setResult(Activity.RESULT_OK, result)
                    activity?.finish()

                }
                Status.ERROR -> {
                    hideProgress()
                    toast(context?.getString(endpass.android.endpass_sdk.R.string.error_text)!!)
                }
            }
        })

        oAuthViewModel.settingsLiveData.observe(this, Observer {
            authViewModel.requestCode(it.email)
            showDialog()

        })

        oAuthViewModel.scopesLiveData.observe(this, Observer {
            showScopeFragment()
        })

    }


    private fun showDialog() {
        verificationFragment =
            VerificationFragment.newInstance(EnumCollections.LoginScenario.OAUTH_CONFIRM.screen)
        verificationFragment.setStateListener {

        }
        childFragmentManager.showDialogFragment(VerificationFragment::class.java.name) {
            verificationFragment
        }
    }


    private fun showScopeFragment() {
        scopeAccessFragment = ScopeAccessFragment()
        childFragmentManager.showDialogFragment(ScopeAccessFragment::class.java.name) {
            scopeAccessFragment
        }
    }


    private fun hideProgress() {
        progressBar.hide()

    }

    private fun showProgress() {
        progressBar.show()
    }

}