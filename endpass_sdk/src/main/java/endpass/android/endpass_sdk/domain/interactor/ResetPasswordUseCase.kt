package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.resetPass.ResetPassResponse
import io.reactivex.Single
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<ResetPassResponse, ResetPasswordUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ResetPassResponse> =
        loginRepository.resetPassword(params)

    data class Params(
        val email: String? = null
    )
}