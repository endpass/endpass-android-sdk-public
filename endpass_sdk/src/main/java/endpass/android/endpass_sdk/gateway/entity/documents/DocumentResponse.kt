package endpass.android.endpass_sdk.gateway.entity.documents

import androidx.annotation.Keep


@Keep
data class DocumentResponse(
    val total: Int,
    val items: List<Document>
)