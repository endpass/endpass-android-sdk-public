package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.EnumCollections
import io.reactivex.Single
import javax.inject.Inject

class RequireDocumentUseCase @Inject constructor(private val mainRepository: MainRepository) :
    SingleUseCase<List<EnumCollections.DocumentType>, RequireDocumentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<EnumCollections.DocumentType>> =
        mainRepository.requireDocuments(params)

    data class Params(
        val clientId: String
    )
}

