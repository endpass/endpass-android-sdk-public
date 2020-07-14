package endpass.android.endpass_sdk.gateway.entity

import androidx.annotation.Keep


@Keep
data class Photo(
    val albumId: Int,
    val id: Int,
    val thumbnailUrl: String,
    val title: String,
    val url: String
)