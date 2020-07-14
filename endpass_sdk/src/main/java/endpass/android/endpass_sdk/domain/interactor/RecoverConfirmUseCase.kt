package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.recover.RecoverConfirmResponse
import io.reactivex.Single
import javax.inject.Inject

class RecoverConfirmUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<RecoverConfirmResponse, RecoverConfirmUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<RecoverConfirmResponse> =
        loginRepository.confirmRecover(params)

    data class Params(
        val email: String,
        val code: String
    )
}