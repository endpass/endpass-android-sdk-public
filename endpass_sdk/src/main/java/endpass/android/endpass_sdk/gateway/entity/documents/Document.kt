package endpass.android.endpass_sdk.gateway.entity.documents

import android.os.Parcelable
import androidx.annotation.Keep
import endpass.android.endpass_sdk.gateway.EnumCollections
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class Document(
    val id: String,
    val createdAt: Long,
    val documentType: EnumCollections.DocumentType,
    var status: EnumCollections.DocumentStatusType,
    var isSelected: Boolean = false
) : Parcelable