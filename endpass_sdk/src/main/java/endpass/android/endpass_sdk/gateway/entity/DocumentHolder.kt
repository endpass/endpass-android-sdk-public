package endpass.android.endpass_sdk.gateway.entity

import android.os.Parcelable
import androidx.annotation.Keep
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class DocumentHolder(
    val documentType: EnumCollections.DocumentType,
    val desc: String,
    var document: Document? = null,
    var allDocuments: ArrayList<Document> = ArrayList()
) : Parcelable {

    fun hasDocument() = document != null

}
