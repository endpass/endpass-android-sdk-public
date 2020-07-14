package endpass.android.endpass_sdk.gateway.entity.login

import androidx.annotation.Keep


@Keep
data class LoginResponse(
    val challenge: Challenge,
    val remembered: Boolean,
    val success: Boolean
)