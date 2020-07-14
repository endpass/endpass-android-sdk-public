package endpass.android.endpass_sdk.gateway.entity.documents

import androidx.annotation.Keep
import endpass.android.endpass_sdk.gateway.EnumCollections


@Keep
data class LoadingFlowData(
    val status: EnumCollections.DocumentStatusType,
    val percent: Int
)