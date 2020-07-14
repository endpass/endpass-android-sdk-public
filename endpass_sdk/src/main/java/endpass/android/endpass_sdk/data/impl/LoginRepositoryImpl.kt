package endpass.android.endpass_sdk.data.impl

import endpass.android.endpass_sdk.data.exception.request
import endpass.android.endpass_sdk.data.exception.requestRedirect
import endpass.android.endpass_sdk.data.remote.ApiService
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.change_pass.ChangePassResponse
import endpass.android.endpass_sdk.gateway.entity.check.CheckResponse
import endpass.android.endpass_sdk.gateway.entity.code.CodeResponse
import endpass.android.endpass_sdk.gateway.entity.confirmPass.ConfirmPassResponse
import endpass.android.endpass_sdk.gateway.entity.login.LoginResponse
import endpass.android.endpass_sdk.gateway.entity.oauth.*
import endpass.android.endpass_sdk.gateway.entity.recover.RecoverConfirmResponse
import endpass.android.endpass_sdk.gateway.entity.recover.RecoverResponse
import endpass.android.endpass_sdk.gateway.entity.resetPass.ResetPassResponse
import endpass.android.endpass_sdk.gateway.entity.verify.VerifyCodeResponse
import io.reactivex.Single

class LoginRepositoryImpl(
    private val api: ApiService
) : LoginRepository {


    override fun login(params: LoginUseCase.Params): Single<LoginResponse> = api.login(params)

    override fun check(params: CheckPasswordUseCase.Params): Single<CheckResponse> =
        request(api.check(params))

    override fun requestCode(params: RequestCodeUseCase.Params): Single<CodeResponse> =
        request(api.requestCode(params))

    override fun verifyCode(params: VerifyEmailUseCase.Params): Single<VerifyCodeResponse> =
        request(api.verifyEmail(params))

    override fun resetPassword(params: ResetPasswordUseCase.Params): Single<ResetPassResponse> =
        request(api.resetPassword(params))

    override fun confirmPassword(params: ConfirmPasswordUseCase.Params): Single<ConfirmPassResponse> =
        request(api.confirmPassword(params))


    override fun changePassword(params: ChangePasswordUseCase.Params): Single<ChangePassResponse> =
        request(api.changePassword(params))


    override fun recover(params: RecoverUseCase.Params): Single<RecoverResponse> =
        request(api.recover(params.email))


    override fun confirmRecover(params: RecoverConfirmUseCase.Params): Single<RecoverConfirmResponse> =
        request(api.confirmRecover(params))


    override fun oauth(params: OauthUseCase.Params): Single<String> =
        requestRedirect<String>(
            api.oauthAuth(
                params.oauthBaseUrl + "/v1/oauth/auth",
                params.client_id,
                params.scope,
                params.state,
                params.response_type,
                params.code_challenge,
                params.code_challenge_method
            )
        )

    override fun oauthLogin(params: GetOauthLoginUseCase.Params): Single<OauthLoginResponse> =
        request(api.oauthLogin(params.challenge))


    override fun oauthSettings(params: OauthSettingsUseCase.Params): Single<OauthSettingsResponse> =
        request(api.oauthSettings())


    override fun oauthLogin(params: PostOauthLoginUseCase.Params): Single<PostOauthLoginResponse> =
        request(api.oauthLogin(params))


    override fun getConsent(params: GetOauthConsentUseCase.Params): Single<String> =
        requestRedirect<String>(api.getConsent(params.url))


    override fun getScopes(params: GetOauthScopesUseCase.Params): Single<ConsentResponse> =
        request(api.getScopesByConsent(params.id))


    override fun postConsent(params: PostOauthConsentUseCase.Params): Single<PostOauthConsentResponse> =
        request(api.postConsent(params))


    override fun getRedirectUrls(params: GetRedirectUrlUseCase.Params): Single<String> =
        requestRedirect<String>(api.getRedirectUrls(params.url))


    override fun oauthPostToken(params: PostOauthTokenUseCase.Params): Single<PostOauthTokenResponse> =
        request(
            api.oauthToken(
                params.oauthBaseUrl + "/v1/oauth/token",
                params.grant_type,
                params.code,
                params.client_id,
                params.code_verifier
            )
        )
}
