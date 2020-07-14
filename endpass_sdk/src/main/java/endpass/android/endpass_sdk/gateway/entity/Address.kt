package endpass.android.endpass_sdk.gateway.entity

import androidx.annotation.Keep


@Keep
data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)