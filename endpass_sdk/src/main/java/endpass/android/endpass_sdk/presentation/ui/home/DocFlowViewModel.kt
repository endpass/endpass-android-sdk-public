package endpass.android.endpass_sdk.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentStatusType.Uploading
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentStatusType.Recognition
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentStatusType.PendingReview
import endpass.android.endpass_sdk.presentation.vo.Resource
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import endpass.android.endpass_sdk.gateway.entity.documents.LoadingFlowData
import endpass.android.endpass_sdk.gateway.entity.documents.UploadStatusResponse
import endpass.android.endpass_sdk.presentation.utils.AppSingleton


open class DocFlowViewModel(
    private val documentsUseCase: GetDocumentsUseCase,
    private val checkDocumentUseCase: CheckDocumentUseCase,
    private val addDocumentUseCase: AddDocumentUseCase,
    private val uploadDocumentUseCase: UploadDocumentUseCase,
    private val uploadDocumentStatusUseCase: UploadDocumentStatusUseCase,
    private val confirmDocumentStatusUseCase: ConfirmDocumentStatusUseCase,
    private val getDocumentUseCase: GetDocumentUseCase,
    private val requireDocumentUseCase: RequireDocumentUseCase
) : ViewModel() {


    var requireDocumentsLiveData: MutableLiveData<List<EnumCollections.DocumentType>> =
        MutableLiveData()
    var documentLiveData: MutableLiveData<Resource<List<Document>>> = MutableLiveData()
    var statusDocumentLiveData: MutableLiveData<Resource<UploadStatusResponse>> = MutableLiveData()
    var uploadDocumentLiveData: MutableLiveData<Resource<LoadingFlowData>> =
        MutableLiveData()

    var uploadStateLiveData: MutableLiveData<LoadingFlowData> =
        MutableLiveData()


    fun getDocuments() {
        documentLiveData.value = Resource.loading(null)
        documentsUseCase.execute(
            { response ->
                documentLiveData.value = Resource.success(response.items)
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, GetDocumentsUseCase.Params(null)
        )
    }

    fun checkDocument(pathList: List<String>, documentType: EnumCollections.DocumentType) {
        uploadDocumentLiveData.value = Resource.loading(null)
        checkDocumentUseCase.execute(
            { response ->
                val isCheck = response.success ?: false
                if (isCheck) {
                    addDocuments(pathList, documentType)
                } else {
                    uploadDocumentLiveData.value = Resource.error("File error", null)
                }

            },
            {
                uploadDocumentLiveData.value = Resource.error(it.message.toString(), null)
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, CheckDocumentUseCase.Params(pathList[0])
        )
    }

    private fun addDocuments(pathList: List<String>, documentType: EnumCollections.DocumentType) {
        addDocumentUseCase.execute(
            { response ->
                val isAdded = response.success ?: false
                if (isAdded) {
                    uploadDocument(pathList, response.message!!)
                    uploadStateLiveData.value = LoadingFlowData(Uploading, 10)
                }
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, AddDocumentUseCase.Params(documentType)
        )
    }

    private fun uploadDocument(
        pathList: List<String>,
        documentId: String,
        isFrontSide: Boolean = true
    ) {
        var path = pathList[0]
        if (!isFrontSide) {
            path = pathList[1]
        }

        uploadDocumentUseCase.execute(
            { response ->
                val isUploaded = response.success ?: false
                if (isUploaded) {
                    if (isFrontSide && pathList.size == 2) {
                        uploadDocument(pathList, documentId, false)
                    } else {
                        //  uploadDocumentLiveData.value =
                        //       Resource.success(LoadingFlowData(RECOGNITION, 50))
                        uploadStateLiveData.value = LoadingFlowData(Uploading, 10)
                        getUploadDocumentStatus(documentId, true)
                    }
                }
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, UploadDocumentUseCase.Params(path, documentId, isFrontSide)
        )
    }


    fun getUploadDocumentStatus(documentId: String, isFlow: Boolean = false) {
        statusDocumentLiveData.value = Resource.loading(null)
        uploadDocumentStatusUseCase.execute(
            { response ->
                if (isFlow) {
                    uploadStateLiveData.value = LoadingFlowData(Recognition, 5)
                    confirm(documentId)
                } else {
                    statusDocumentLiveData.value = Resource.success(response)
                }
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, UploadDocumentStatusUseCase.Params(documentId)
        )
    }


    private fun confirm(documentId: String) {
        confirmDocumentStatusUseCase.execute(
            {
                uploadStateLiveData.value = LoadingFlowData(Recognition, 3)
                getDocument(documentId)
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, ConfirmDocumentStatusUseCase.Params(documentId)
        )
    }


    private fun getDocument(documentId: String) {
        getDocumentUseCase.execute(
            { response ->
                if (response.status == PendingReview) {
                    uploadStateLiveData.value = LoadingFlowData(PendingReview, 0)
                } else {
                    getDocument(documentId)
                    uploadStateLiveData.value = LoadingFlowData(Recognition, 1)

                }
            },
            {
                documentLiveData.value = Resource.error(it.message.toString(), null)

            }, GetDocumentUseCase.Params(documentId)
        )
    }


    fun getRequireDocuments() {
        requireDocumentUseCase.execute(
            { response ->
                requireDocumentsLiveData.value = response
                AppSingleton.filters = response
            },
            {
              //  requireDocumentsLiveData.value = ""

            }, RequireDocumentUseCase.Params(AppSingleton.clientId)
        )
    }


}

