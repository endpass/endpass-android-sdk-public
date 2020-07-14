package endpass.android.endpass_sdk.gateway.entity.login

import android.os.Parcelable
import androidx.annotation.Keep
import endpass.android.endpass_sdk.gateway.EnumCollections
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
data class LoginExtraData(
    var email: String? = null,
    var password: String? = null,
    var challengeType: String? = null,
    var code: String? = null,
    var recoverCode: String? = null,
    var scenario: EnumCollections.LoginScenario? = null,
    var documentId: String? = null
    ) : Parcelable