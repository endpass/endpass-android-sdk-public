package endpass.android.endpass_sdk.gateway.entity.oauth

import androidx.annotation.Keep


@Keep
data class ConsentResponse(
    val requested_scope: ArrayList<String>,
    val skip: Boolean,
    val redirectURL: String,
    val client: Client

)

@Keep
data class Client(

    val redirect_uris: ArrayList<String>

)