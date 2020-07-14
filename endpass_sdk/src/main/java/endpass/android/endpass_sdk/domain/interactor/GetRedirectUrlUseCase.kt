package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import io.reactivex.Single
import javax.inject.Inject

class GetRedirectUrlUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<String, GetRedirectUrlUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<String> =
        loginRepository.getRedirectUrls(params)

    data class Params(
        val url: String
    )
}