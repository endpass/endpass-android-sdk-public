package endpass.android.endpass_sdk.gateway.entity.oauth

import androidx.annotation.Keep


@Keep
data class OauthLoginResponse(
    val skip: Boolean,
    val email: String,
    val redirect: String?
)