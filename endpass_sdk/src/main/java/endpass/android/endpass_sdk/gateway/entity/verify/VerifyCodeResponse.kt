package endpass.android.endpass_sdk.gateway.entity.verify

import androidx.annotation.Keep


@Keep
data class VerifyCodeResponse(
    val csrfToken: String?,
    val expiresIn: Long?,
    val success: Boolean?
)