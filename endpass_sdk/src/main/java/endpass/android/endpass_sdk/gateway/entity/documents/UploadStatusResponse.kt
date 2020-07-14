package endpass.android.endpass_sdk.gateway.entity.documents

import androidx.annotation.Keep
import endpass.android.endpass_sdk.gateway.EnumCollections

@Keep
data class UploadStatusResponse(
    val back: Back,
    val front: Front
){

    fun isBackFileUploaded() = back.status == EnumCollections.UploadFileStatusType.Uploaded.name

    fun isFrontFileUploaded() = front.status == EnumCollections.UploadFileStatusType.Uploaded.name

}