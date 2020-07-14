package endpass.android.endpass_sdk.gateway.entity.oauth

import androidx.annotation.Keep


@Keep
data class OauthSettingsResponse(
    val emailConfirmed: Boolean,
    val otpEnabled: Boolean,
    val smsCodeEnabled: Boolean,
    val email: String
)