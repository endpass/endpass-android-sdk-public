package endpass.android.endpass_sdk.presentation.utils

import android.text.TextUtils
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AddCookiesInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val builder = chain.request().newBuilder()

        val cookies = CookeManagerSingleton.msCookieManager.cookieStore.cookies
        if (cookies.size > 0) {
            // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
            val cookie = TextUtils.join(";", cookies)
            builder.addHeader("Cookie", cookie)
        }

        return chain.proceed(builder.build())

    }


}
