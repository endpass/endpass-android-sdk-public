package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import io.reactivex.Single
import javax.inject.Inject

class GetOauthConsentUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<String, GetOauthConsentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<String> =
        loginRepository.getConsent(params)

    data class Params(
        val url: String
    )
}