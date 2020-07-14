package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import io.reactivex.Single
import javax.inject.Inject

class OauthUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<String, OauthUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<String> =
        loginRepository.oauth(params)

    data class Params(
        val client_id: String,
        val scope: String,
        val state: String,
        val response_type: String = "code",
        val code_challenge: String,
        val code_challenge_method: String,
        val  oauthBaseUrl:String
    )
}