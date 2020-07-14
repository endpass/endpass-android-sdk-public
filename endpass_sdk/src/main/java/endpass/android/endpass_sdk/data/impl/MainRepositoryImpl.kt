package endpass.android.endpass_sdk.data.impl

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import endpass.android.endpass_sdk.data.exception.request
import endpass.android.endpass_sdk.data.remote.ApiService
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.domain.repository.MainRepository
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.*
import io.reactivex.Single
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class MainRepositoryImpl(
    private val api: ApiService
) : MainRepository {


    override fun document(params: GetDocumentUseCase.Params): Single<Document> =
        request(api.getDocumentById(params.documentId))


    override fun confirm(params: ConfirmDocumentStatusUseCase.Params): Single<DocumentFlowResponse> =
        request(api.confirmDocument(params.documentId))


    override fun uploadStatus(params: UploadDocumentStatusUseCase.Params): Single<UploadStatusResponse> =
        request(api.uploadDocumentStatus(params.documentId))


    override fun uploadDocument(params: UploadDocumentUseCase.Params): Single<CheckDocumentResponse> {
        val body = createFormData(params.path)
        var side = "front"
        if (!params.isFrontSide) {
            side = "back"
        }
        return request(api.uploadDocument(body, params.idDocument, side))
    }

    override fun addDocument(params: AddDocumentUseCase.Params): Single<CheckDocumentResponse> =
        request(api.addDocument(params))

    override fun checkDocument(params: CheckDocumentUseCase.Params): Single<CheckDocumentResponse> {
        val body = createFormData(params.path)
        return request(api.checkDocument(body))

    }

    override fun documents(params: GetDocumentsUseCase.Params): Single<DocumentResponse> =
        request(api.documents())

    override fun deleteDocument(params: DeleteDocumentUseCase.Params): Single<DocumentFlowResponse> =
        request(api.deleteDocument(params.documentId, params.code))


    private fun createFormData(path: String): MultipartBody.Part {
        val file = File(path)
        file.createNewFile()

        //Convert bitmap to byte array

        val bos = ByteArrayOutputStream()
        val bitmap = bitmap(file)
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        val bitmapdata = bos.toByteArray()


        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        val requestFile = RequestBody.create(MediaType.parse("image/*"), file)
        return MultipartBody.Part.createFormData("file", file.name, requestFile)
    }

    private fun bitmap(file: File): Bitmap? {

        if (file.exists()) {

            val matrix = Matrix()
            //  matrix.postRotate(180f)
            val scaledBitmap = BitmapFactory.decodeFile(file.absolutePath)

            return Bitmap.createBitmap(
                scaledBitmap,
                0,
                0,
                scaledBitmap.width,
                scaledBitmap.height,
                matrix,
                true
            )

        }
        return null
    }


    override fun requireDocuments(params: RequireDocumentUseCase.Params): Single<List<EnumCollections.DocumentType>> =
        request(api.requireDocuments(params.clientId))


}
