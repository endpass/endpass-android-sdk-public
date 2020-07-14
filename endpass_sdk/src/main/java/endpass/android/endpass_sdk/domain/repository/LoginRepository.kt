package endpass.android.endpass_sdk.domain.repository

import endpass.android.endpass_sdk.domain.interactor.*
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

interface LoginRepository {

    fun login(params: LoginUseCase.Params): Single<LoginResponse>

    fun check(params: CheckPasswordUseCase.Params): Single<CheckResponse>

    fun requestCode(params: RequestCodeUseCase.Params): Single<CodeResponse>

    fun verifyCode(params: VerifyEmailUseCase.Params): Single<VerifyCodeResponse>

    fun resetPassword(params: ResetPasswordUseCase.Params): Single<ResetPassResponse>

    fun confirmPassword(params: ConfirmPasswordUseCase.Params): Single<ConfirmPassResponse>

    fun changePassword(params: ChangePasswordUseCase.Params): Single<ChangePassResponse>

    fun recover(params: RecoverUseCase.Params): Single<RecoverResponse>

    fun confirmRecover(params: RecoverConfirmUseCase.Params): Single<RecoverConfirmResponse>


    /**
     * Oauth
     */

    fun oauth(params: OauthUseCase.Params): Single<String>

    fun oauthLogin(params: GetOauthLoginUseCase.Params): Single<OauthLoginResponse>

    fun oauthSettings(params: OauthSettingsUseCase.Params): Single<OauthSettingsResponse>

    fun oauthLogin(params: PostOauthLoginUseCase.Params): Single<PostOauthLoginResponse>

    fun getConsent(params: GetOauthConsentUseCase.Params): Single<String>

    fun getScopes(params: GetOauthScopesUseCase.Params): Single<ConsentResponse>

    fun postConsent(params: PostOauthConsentUseCase.Params): Single<PostOauthConsentResponse>

    fun getRedirectUrls(params: GetRedirectUrlUseCase.Params): Single<String>

    fun oauthPostToken(params: PostOauthTokenUseCase.Params): Single<PostOauthTokenResponse>



}