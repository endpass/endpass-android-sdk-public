package endpass.android.endpass_sdk.data.remote


import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.change_pass.ChangePassResponse
import endpass.android.endpass_sdk.gateway.entity.check.CheckResponse
import endpass.android.endpass_sdk.gateway.entity.code.CodeResponse
import endpass.android.endpass_sdk.gateway.entity.confirmPass.ConfirmPassResponse
import endpass.android.endpass_sdk.gateway.entity.documents.*
import endpass.android.endpass_sdk.gateway.entity.login.LoginResponse
import endpass.android.endpass_sdk.gateway.entity.oauth.*
import endpass.android.endpass_sdk.gateway.entity.recover.RecoverConfirmResponse
import endpass.android.endpass_sdk.gateway.entity.recover.RecoverResponse
import endpass.android.endpass_sdk.gateway.entity.resetPass.ResetPassResponse
import endpass.android.endpass_sdk.gateway.entity.verify.VerifyCodeResponse
import io.reactivex.Single
import okhttp3.MultipartBody
import retrofit2.Response

import retrofit2.http.*
import retrofit2.http.Url


interface ApiService {

    @POST("/api/v1.1/auth")
    fun login(@Body params: LoginUseCase.Params): Single<LoginResponse>

    @POST("/api/v1.1/regular-password/check")
    fun check(@Body params: CheckPasswordUseCase.Params): Single<Response<CheckResponse>>

    @POST("/api/v1.1/auth/code")
    fun requestCode(@Body params: RequestCodeUseCase.Params): Single<Response<CodeResponse>>

    @POST("/api/v1.1/auth/token")
    fun verifyEmail(@Body params: VerifyEmailUseCase.Params): Single<Response<VerifyCodeResponse>>

    @POST("/api/v1.1/regular-password/reset")
    fun resetPassword(@Body params: ResetPasswordUseCase.Params): Single<Response<ResetPassResponse>>

    @POST("/api/v1.1/auth/signup")
    fun confirmPassword(@Body params: ConfirmPasswordUseCase.Params): Single<Response<ConfirmPassResponse>>

    @POST("/api/v1.1/regular-password/reset/confirm")
    fun changePassword(@Body params: ChangePasswordUseCase.Params): Single<Response<ChangePassResponse>>

    @GET("/api/v1.1/documents")
    fun documents(): Single<Response<DocumentResponse>>

    @GET("/api/v1.1/documents/{id}")
    fun getDocumentById(@Path("id") id: String): Single<Response<Document>>

    @GET("/api/v1.1/auth/recover")
    fun recover(@Query("email") email: String?): Single<Response<RecoverResponse>>

    @POST("/api/v1.1/auth/recover")
    fun confirmRecover(@Body params: RecoverConfirmUseCase.Params): Single<Response<RecoverConfirmResponse>>

    @DELETE("/api/v1.1/documents/{id}")
    fun deleteDocument(@Path("id") id: String, @Header("X-Request-Code") code: String):
            Single<Response<DocumentFlowResponse>>

    @Multipart
    @POST("/api/v1.1/documents/file/check")
    fun checkDocument(@Part file: MultipartBody.Part): Single<
            Response<CheckDocumentResponse>>

    @POST("/api/v1.1/documents")
    fun addDocument(@Body params: AddDocumentUseCase.Params): Single<
            Response<CheckDocumentResponse>>

    @Multipart
    @POST("/api/v1.1/documents/{id}/{side}")
    fun uploadDocument(@Part file: MultipartBody.Part, @Path("id") id: String, @Path("side") side: String): Single<
            Response<CheckDocumentResponse>>


    @GET("/api/v1.1/documents/{id}/status/upload")
    fun uploadDocumentStatus(@Path("id") id: String): Single<Response<UploadStatusResponse>>


    @GET("/api/v1.1/apps/{id}/documents/required")
    fun requireDocuments(@Path("id") id: String): Single<Response<List<EnumCollections.DocumentType>>>


    @POST("/api/v1.1/documents/{id}/confirm")
    fun confirmDocument(@Path("id") id: String): Single<
            Response<DocumentFlowResponse>>


    @GET
    fun oauthAuth(
        @Url url: String,
        @Query("client_id") id: String?,
        @Query("scope") scope: String?,
        @Query("state") state: String?,
        @Query("response_type") response_type: String?,
        @Query("code_challenge") code_challenge: String?,
        @Query("code_challenge_method") code_challenge_method: String?
    ): Single<Response<String>>


    @GET("/api/v1.1/oauth/login")
    fun oauthLogin(@Query("challenge") challenge: String?): Single<Response<OauthLoginResponse>>


    @GET("/api/v1.1/settings")
    fun oauthSettings(): Single<Response<OauthSettingsResponse>>


    @POST("/api/v1.1/oauth/login")
    fun oauthLogin(@Body params: PostOauthLoginUseCase.Params): Single<
            Response<PostOauthLoginResponse>>

    @GET
    fun getConsent(@Url url: String): Single<Response<String>>

    @GET("/api/v1.1/oauth/consent/{id}")
    fun getScopesByConsent(@Path("id") id: String): Single<Response<ConsentResponse>>


    @POST("/api/v1.1/oauth/consent")
    fun postConsent(@Body params: PostOauthConsentUseCase.Params): Single<
            Response<PostOauthConsentResponse>>

    @GET
    fun getRedirectUrls(@Url url: String): Single<Response<String>>

    @FormUrlEncoded
    @POST
    fun oauthToken(
        @Url url: String,
        @Field("grant_type") grant_type: String,
        @Field("code") code: String,
        @Field("client_id") client_id: String,
        @Field("code_verifier") code_verifier: String
    ): Single<
            Response<PostOauthTokenResponse>>
}