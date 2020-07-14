package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.CheckDocumentResponse
import io.reactivex.Single
import javax.inject.Inject

class UploadDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<CheckDocumentResponse, UploadDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CheckDocumentResponse> =
        mainRepository.uploadDocument(params)

    data class Params(
        val path: String,
        val idDocument: String,
        val isFrontSide: Boolean
    )
}

