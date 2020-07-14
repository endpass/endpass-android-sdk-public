package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.check.CheckResponse
import io.reactivex.Single
import javax.inject.Inject

class CheckPasswordUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<CheckResponse, CheckPasswordUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CheckResponse> =
        loginRepository.check(params)

    data class Params(
        val email: String? = null
    )
}