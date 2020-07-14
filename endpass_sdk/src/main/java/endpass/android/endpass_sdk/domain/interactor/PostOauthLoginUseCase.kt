package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.oauth.PostOauthLoginResponse
import io.reactivex.Single
import javax.inject.Inject

class PostOauthLoginUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<PostOauthLoginResponse, PostOauthLoginUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<PostOauthLoginResponse> =
        loginRepository.oauthLogin(params)

    data class Params(
        val challenge: String,
        val code: String
    )
}