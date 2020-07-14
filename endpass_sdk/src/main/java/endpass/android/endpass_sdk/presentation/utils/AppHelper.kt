package endpass.android.endpass_sdk.presentation.utils

import android.net.UrlQuerySanitizer


object AppHelper {


    fun getParamValueFromUrl(httpUrl: String, queryParam: String): String {
        val sanitizer = UrlQuerySanitizer(httpUrl)
        return sanitizer.getValue(queryParam)
    }


}
