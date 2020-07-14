package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.confirmPass.ConfirmPassResponse
import io.reactivex.Single
import javax.inject.Inject

class ConfirmPasswordUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<ConfirmPassResponse, ConfirmPasswordUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ConfirmPassResponse> =
        loginRepository.confirmPassword(params)

    data class Params(
        val email: String? = null,
        val password: String? = null,
        val challengeType: String? = null,
        val code: String? = null
    )
}