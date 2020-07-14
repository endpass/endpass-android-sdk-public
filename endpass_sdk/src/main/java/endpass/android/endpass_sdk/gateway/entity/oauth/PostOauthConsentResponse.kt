package endpass.android.endpass_sdk.gateway.entity.oauth

import androidx.annotation.Keep


@Keep
data class PostOauthConsentResponse(
    val redirect: String
)