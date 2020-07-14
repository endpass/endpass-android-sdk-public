package endpass.android.endpass_sdk.gateway.entity.documents

import androidx.annotation.Keep

@Keep
data class CheckDocumentResponse(
    val success: Boolean?,
    val message: String?
)