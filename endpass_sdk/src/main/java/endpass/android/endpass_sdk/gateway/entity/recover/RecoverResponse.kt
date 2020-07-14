package endpass.android.endpass_sdk.gateway.entity.recover

import androidx.annotation.Keep

@Keep
data class RecoverResponse(
    val success: String,
    val message: String
)