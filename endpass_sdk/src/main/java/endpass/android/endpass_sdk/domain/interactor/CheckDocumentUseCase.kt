package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.entity.documents.CheckDocumentResponse
import io.reactivex.Single
import javax.inject.Inject

class CheckDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<CheckDocumentResponse, CheckDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CheckDocumentResponse> =
        mainRepository.checkDocument(params)

    data class Params(
        val path: String
    )
}

