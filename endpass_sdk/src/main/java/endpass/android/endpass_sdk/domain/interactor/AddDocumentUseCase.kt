package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.CheckDocumentResponse
import io.reactivex.Single
import javax.inject.Inject

class AddDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<CheckDocumentResponse, AddDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CheckDocumentResponse> =
        mainRepository.addDocument(params)

    data class Params(
        val type: EnumCollections.DocumentType
    )
}

