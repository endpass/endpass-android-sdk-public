package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import io.reactivex.Single
import javax.inject.Inject

class GetDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<Document, GetDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Document> =
        mainRepository.document(params)

    data class Params(
        val documentId: String
    )
}

