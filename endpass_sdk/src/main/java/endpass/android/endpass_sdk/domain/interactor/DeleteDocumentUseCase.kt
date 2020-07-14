package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.DocumentFlowResponse
import io.reactivex.Single
import javax.inject.Inject

class DeleteDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<DocumentFlowResponse, DeleteDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<DocumentFlowResponse> =
        mainRepository.deleteDocument(params)

    data class Params(
        val documentId: String,
        val code: String
    )
}

