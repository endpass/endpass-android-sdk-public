package endpass.android.endpass_sdk.presentation.utils

import android.content.Context
import endpass.android.endpass_sdk.presentation.ext.networkInfo

class NetworkHandler(private val context: Context) {
    val isConnected get() = context.networkInfo?.isConnectedOrConnecting ?: true
}