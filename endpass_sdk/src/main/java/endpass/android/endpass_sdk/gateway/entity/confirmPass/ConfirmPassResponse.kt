package endpass.android.endpass_sdk.gateway.entity.confirmPass

import androidx.annotation.Keep

@Keep
data class ConfirmPassResponse(
    val csrfToken: String?,
    val expiresIn: Long?,
    val success: Boolean?
)