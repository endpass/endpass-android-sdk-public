package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.change_pass.ChangePassResponse
import io.reactivex.Single
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<ChangePassResponse, ChangePasswordUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<ChangePassResponse> =
        loginRepository.changePassword(params)

    data class Params(
        val passwordResetToken: String? = null,
        val newPassword: String? = null
    )
}