package endpass.android.endpass_sdk.presentation.ui.auth
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import endpass.android.endpass_sdk.presentation.vo.Resource
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.gateway.entity.oauth.ConsentResponse
import endpass.android.endpass_sdk.gateway.entity.oauth.OauthSettingsResponse
import endpass.android.endpass_sdk.gateway.entity.oauth.PostOauthTokenResponse
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.utils.AppHelper
import endpass.android.endpass_sdk.presentation.utils.AppSingleton
import java.lang.Exception


open class OAuthViewModel(
    private val oauthUseCase: OauthUseCase,
    private val getOauthLoginUseCase: GetOauthLoginUseCase,
    private val oauthSettingsUseCase: OauthSettingsUseCase,
    private val postOauthLoginUseCase: PostOauthLoginUseCase,
    private val getOauthConsentUseCase: GetOauthConsentUseCase,
    private val getOauthScopesUseCase: GetOauthScopesUseCase,
    private val postOauthConsentUseCase: PostOauthConsentUseCase,
    private val getRedirectUrlUseCase: GetRedirectUrlUseCase,
    private val oauthTokenUseCase: PostOauthTokenUseCase
) : ViewModel() {


    var oauthFlowLiveData: MutableLiveData<Resource<PostOauthTokenResponse>> = MutableLiveData()
    var settingsLiveData: MutableLiveData<OauthSettingsResponse> = MutableLiveData()
    var scopesLiveData: MutableLiveData<ConsentResponse> = MutableLiveData()
    private var scopes = arrayListOf<String>()

    var consentChallenge: String? = null

    fun oauth() {
        oauthFlowLiveData.value = Resource.loading(null)
        oauthUseCase.execute(
            {
                AppSingleton.challenge =
                    AppHelper.getParamValueFromUrl(it, "login_challenge")
                oauthLogin(AppSingleton.challenge ?: "")
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, OauthUseCase.Params(
                AppSingleton.clientId,
                AppSingleton.scope,
                AppSingleton.codeVerifier!!,
                AppSingleton.responseType,
                AppSingleton.codeChallenge!!,
                AppSingleton.codeChallengeMethod,
                Constant.OAUTH_URL
            )
        )
    }


    private fun oauthLogin(challenge: String) {
        getOauthLoginUseCase.execute(
            {
                if (it.redirect != null) {
                    getConsent(it.redirect!!)
                } else {
                    getSettings()
                }
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, GetOauthLoginUseCase.Params(
                challenge
            )
        )
    }


    private fun getSettings() {
        oauthSettingsUseCase.execute(
            {
                settingsLiveData.value = it
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, OauthSettingsUseCase.Params(
                null
            )
        )
    }


    fun oauthLogin(challenge: String?, code: String?) {
        postOauthLoginUseCase.execute(
            {
                getConsent(it.redirect)
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, PostOauthLoginUseCase.Params(
                challenge!!.trim(), code!!.trim()
            )
        )
    }


    private fun getConsent(url: String) {
        getOauthConsentUseCase.execute(
            {
                try {
                    val consentChallenge = AppHelper.getParamValueFromUrl(it, "consent_challenge")
                    getScopes(consentChallenge)
                } catch (e: Exception) {
                    oauthFlowLiveData.value = Resource.error("Something went wrong")
                }
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, GetOauthConsentUseCase.Params(
                url
            )
        )
    }


    private fun getScopes(consentChallenge: String) {
        this.consentChallenge = consentChallenge
        getOauthScopesUseCase.execute(
            {
                if (it.skip) {
                    postConsent(it.requested_scope)
                } else {
                    scopesLiveData.value = it
                }
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, GetOauthScopesUseCase.Params(
                consentChallenge
            )
        )
    }


    fun postConsent(scopes: ArrayList<String>) {
        this.scopes = scopes
        postOauthConsentUseCase.execute(
            {
                getRedirectUrls(it.redirect)
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, PostOauthConsentUseCase.Params(
                consentChallenge!!, scopes
            )
        )
    }


    private fun getRedirectUrls(url: String) {
        getRedirectUrlUseCase.execute(
            {
                val code = AppHelper.getParamValueFromUrl(it, "code")
                postToken(code)
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, GetRedirectUrlUseCase.Params(
                url
            )
        )
    }


    private fun postToken(code: String) {
        oauthTokenUseCase.execute(
            {
                oauthFlowLiveData.postValue(Resource.success(it))
            },
            {
                oauthFlowLiveData.value = Resource.error(error = it)

            }, PostOauthTokenUseCase.Params(
                grant_type = "authorization_code",
                code = code,
                client_id = AppSingleton.clientId,
                code_verifier = AppSingleton.codeVerifier?.trim()!!,
                oauthBaseUrl = Constant.OAUTH_URL
            )
        )
    }

}