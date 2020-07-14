package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.DocumentFlowResponse
import io.reactivex.Single
import javax.inject.Inject

class ConfirmDocumentStatusUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<DocumentFlowResponse, ConfirmDocumentStatusUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<DocumentFlowResponse> =
        mainRepository.confirm(params)

    data class Params(
        val documentId: String
    )
}

