package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.DocumentResponse
import io.reactivex.Single
import javax.inject.Inject

class GetDocumentsUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<DocumentResponse, GetDocumentsUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<DocumentResponse> =
        mainRepository.documents(params)

    data class Params(
        val email: String? = null
    )
}