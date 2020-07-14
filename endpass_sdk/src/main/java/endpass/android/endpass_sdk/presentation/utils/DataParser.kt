package endpass.android.endpass_sdk.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentType.Passport
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentType.DriverLicense
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentType.ProofOfAddress
import endpass.android.endpass_sdk.gateway.EnumCollections.DocumentType.IdCard
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import endpass.android.endpass_sdk.presentation.base.Constant
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream


object DataParser {

    private var savedDocuments: List<Document> = listOf()

    fun getSavedDocuments() = savedDocuments

    fun updateDocuments(newDocuments: ArrayList<Document>) {

        val selectedDocuments = savedDocuments.filter { it.isSelected }

        newDocuments.forEach { newDocument ->

            selectedDocuments.forEach { currentDocument ->

                if (newDocument.id == currentDocument.id) {
                    newDocument.isSelected = true
                }
            }
        }

        savedDocuments = newDocuments
    }


    /**
     *
     * @return last Document with bad status (NotReadable,NotVerified)
     *
     */

    fun getLastDocumentWithBadStatus(): Document? {

        val filterBadStatus = listOf(
            EnumCollections.DocumentStatusType.NotReadable,
            EnumCollections.DocumentStatusType.NotVerified
        )

        return savedDocuments
            .filter { it.isSelected }
            .filter { it.status in filterBadStatus }
            .maxBy { it.createdAt }

    }

    fun updateSavedDocuments(
        newDocuments: ArrayList<Document>,
        documentType: EnumCollections.DocumentType
    ) {
        updateDocuments(newDocuments)
        savedDocuments.filter { it.documentType == documentType }.forEach {
            it.isSelected = false
        }
        savedDocuments.filter { it.documentType == documentType }.maxBy { it.createdAt }!!.isSelected =
            true
        print(savedDocuments)

    }


    fun selectDocument(selectedDocument: Document) {

        savedDocuments.forEach {
            if (it.documentType == selectedDocument.documentType) {
                it.isSelected = false
                if (it.id == selectedDocument.id) {
                    it.isSelected = true
                }
            }
        }
    }


    /**
     * @return true if all required documents are selected
     */


    fun isAllDocumentsSelected(
    ): Boolean {

        val selectedDocuments = getSavedDocuments().filter { it.isSelected }

        return selectedDocuments.size == AppSingleton.filters!!.size


    }


    /**
     * @return true if selected documents are Verified or PendingReview status
     */

    fun isSelectedDocumentsHasPositiveStatus(): Boolean {


        var uniqueDocumentType: EnumCollections.DocumentType? = null
        var uniqueCount = 0

        val selectedDocuments = savedDocuments.sortedBy { it.documentType }.filter { it.isSelected }

        selectedDocuments.forEach {

            if (it.documentType != uniqueDocumentType && isPositiveStatus(it.status)) {
                uniqueCount++
                uniqueDocumentType = it.documentType
            }
        }

        if (uniqueCount == AppSingleton.filters?.size) {
            return true
        }

        return false

    }


    /**
     * @return true if selected documents contains one more of PendingReview status document
     */

    fun hasPendingReview(): Boolean {

        val selectedDocuments = savedDocuments
            .sortedBy { it.documentType }
            .filter { it.isSelected }
            .filter { it.status == EnumCollections.DocumentStatusType.Verified }

        if (selectedDocuments.size != AppSingleton.filters?.size) {
            return true
        }

        return false

    }

    private fun isPositiveStatus(status: EnumCollections.DocumentStatusType) =
        status == EnumCollections.DocumentStatusType.Verified || status == EnumCollections.DocumentStatusType.PendingReview


    fun getVerifiedDocumentsByDocumentType(documentType: EnumCollections.DocumentType): List<Document> =
        savedDocuments.filter { it.documentType == documentType }.filter { it.status == EnumCollections.DocumentStatusType.Verified }


    fun createDocumentHolders(filters: List<EnumCollections.DocumentType>? = null): MutableList<DocumentHolder> {

        val holderList = mutableListOf<DocumentHolder>()

        if (filters == null) {
            holderList.add(DocumentHolder(Passport, "Not uploaded yet"))
            holderList.add(DocumentHolder(DriverLicense, "Not uploaded yet"))
            holderList.add(DocumentHolder(ProofOfAddress, "Not uploaded yet"))
            holderList.add(DocumentHolder(IdCard, "Not uploaded yet"))
        } else {
            filters.forEach {
                holderList.add(DocumentHolder(it, "Not added yet"))
            }
        }

        return holderList

    }


    fun createVerifiedDocumentHolders(documents: List<Document>): MutableList<DocumentHolder> {

        val holderList = mutableListOf<DocumentHolder>()
        documents.forEach {
            holderList.add(DocumentHolder(it.documentType, it.status.desc, it))
        }

        return holderList

    }


    fun mergeDocumentsToHolders(
        documents: List<Document>,
        documentHolders: MutableList<DocumentHolder>
    ) {

        documents.forEach { document ->
            documentHolders.forEach { documentHolder ->
                if (document.documentType == documentHolder.documentType) {
                    documentHolder.document = document
                    documentHolder.allDocuments.add(document)
                }
            }
        }
    }

    fun mergeSelectedDocumentsToHolders(
        documents: List<Document>,
        documentHolders: MutableList<DocumentHolder>
    ) {

        documents.forEach { document ->
            documentHolders.forEach { documentHolder ->
                if (document.documentType == documentHolder.documentType) {
                    if (document.isSelected) {
                        documentHolder.document = document
                    }
                }
            }
        }
    }

    fun createDocumentUrl(documentHolder: DocumentHolder, isFront: Boolean = true): String {
        var folder = "/front/file"
        if (!isFront) {
            folder = "/back/file"
        }
        return Constant.DOCUMENT_URL + documentHolder.document!!.id + folder

    }


    fun getBitmapFromPath(path: String): Bitmap? {

        val imgFile = File(path)

        if (imgFile.exists()) {

            val matrix = Matrix()
            matrix.postRotate(180f)
            val scaledBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

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

    fun createFile(bitmap: Bitmap?, context: Context): String {

        val time = System.currentTimeMillis()


        val file = File(context.cacheDir, "croppedPath$time")
        file.createNewFile()
        //Convert bitmap to byte array

        val bos = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()


        //write the bytes in file
        val fos = FileOutputStream(file)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()

        return file.path

    }


    const val UserAddress = "user:address:read"
    const val UserPhone = "user:phone:read"
    const val UserEmail = "user:email:read"
    const val UserData = "user:data:read"
    const val DocumentsStatus = "savedDocuments:status:read"
    const val DocumentsImage = "savedDocuments:image:read"
    const val DocumentsData = "savedDocuments:data:read"
    const val WalletAccounts = "wallet:accounts:read"
    const val WalletAddress = "wallet:address:read"
    const val DriverLicenseStatus = "savedDocuments:driver_license:status:read"
    const val ReadID = "savedDocuments:id_card:status:read"
    const val ReadPassport = "savedDocuments:passport:status:read"
    const val ReadProofAddress = "savedDocuments:proof_address:status:read"


    fun getLocalizeScope(key: String): String =

        when (key) {
            UserAddress -> "Read address"
            UserPhone -> "Read phone"
            UserEmail -> "Read email"
            UserData -> "Read user information"
            DocumentsStatus -> "Read document status"
            DocumentsImage -> "Read image savedDocuments"
            DocumentsData -> "Read document content"
            WalletAccounts -> "Read only wallet Accounts data"
            WalletAddress -> "Read only wallet Address data"
            DriverLicenseStatus -> "Read driver license status"
            ReadID -> "Read ID card status"
            ReadPassport -> "Read passport status"
            ReadProofAddress -> "Read proof of address status"
            else -> key
        }

}
