package endpass.android.endpass_sdk.gateway.entity.documents

import androidx.annotation.Keep

@Keep
data class Front(
    val message: String,
    val status: String
)