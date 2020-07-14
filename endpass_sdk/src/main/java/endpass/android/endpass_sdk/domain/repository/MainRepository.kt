package endpass.android.endpass_sdk.domain.repository

import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.*
import io.reactivex.Single

interface MainRepository {

    fun documents(params: GetDocumentsUseCase.Params): Single<DocumentResponse>

    fun deleteDocument(params: DeleteDocumentUseCase.Params): Single<DocumentFlowResponse>

    fun checkDocument(params: CheckDocumentUseCase.Params): Single<CheckDocumentResponse>

    fun addDocument(params: AddDocumentUseCase.Params): Single<CheckDocumentResponse>

    fun uploadDocument(params: UploadDocumentUseCase.Params): Single<CheckDocumentResponse>

    fun uploadStatus(params: UploadDocumentStatusUseCase.Params): Single<UploadStatusResponse>

    fun confirm(params: ConfirmDocumentStatusUseCase.Params): Single<DocumentFlowResponse>

    fun document(params: GetDocumentUseCase.Params): Single<Document>

    fun requireDocuments(params: RequireDocumentUseCase.Params): Single<List<EnumCollections.DocumentType>>


}