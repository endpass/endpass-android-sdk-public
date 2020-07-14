package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.code.CodeResponse
import io.reactivex.Single
import javax.inject.Inject

class RequestCodeUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<CodeResponse, RequestCodeUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CodeResponse> =
        loginRepository.requestCode(params)

    data class Params(
        val email: String? = null
    )
}