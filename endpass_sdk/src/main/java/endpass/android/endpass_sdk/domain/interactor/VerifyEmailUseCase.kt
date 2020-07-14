package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.verify.VerifyCodeResponse
import io.reactivex.Single
import javax.inject.Inject

class VerifyEmailUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<VerifyCodeResponse, VerifyEmailUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<VerifyCodeResponse> =
        loginRepository.verifyCode(params)

    data class Params(

        val email: String? = null,
        val password: String? = null,
        val challengeType: String? = null,
        val code: String? = null
    )
}

