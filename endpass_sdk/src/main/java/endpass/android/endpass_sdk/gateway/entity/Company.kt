package endpass.android.endpass_sdk.gateway.entity

import androidx.annotation.Keep


@Keep
data class Company(
    val bs: String,
    val catchPhrase: String,
    val name: String
)