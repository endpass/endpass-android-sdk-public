package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.oauth.ConsentResponse
import io.reactivex.Single
import javax.inject.Inject

class GetOauthScopesUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<ConsentResponse, GetOauthScopesUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ConsentResponse> =
        loginRepository.getScopes(params)

    data class Params(
        val id: String
    )
}