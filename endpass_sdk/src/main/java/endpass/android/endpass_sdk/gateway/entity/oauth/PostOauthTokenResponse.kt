package endpass.android.endpass_sdk.gateway.entity.oauth

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
data class PostOauthTokenResponse(
    val access_token: String,
    val scope: String
):Parcelable