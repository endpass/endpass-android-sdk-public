package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.UploadStatusResponse
import io.reactivex.Single
import javax.inject.Inject

class UploadDocumentStatusUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<UploadStatusResponse, UploadDocumentStatusUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<UploadStatusResponse> =
        mainRepository.uploadStatus(params)

    data class Params(
        val documentId: String
    )
}

