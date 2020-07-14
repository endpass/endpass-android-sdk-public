package endpass.android.endpass_sdk.gateway.entity.code

import androidx.annotation.Keep

@Keep
data class CodeResponse(
    val timeout: Int,
    var code: String?
)