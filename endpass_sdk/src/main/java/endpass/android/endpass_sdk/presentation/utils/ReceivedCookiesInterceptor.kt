package endpass.android.endpass_sdk.presentation.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import endpass.android.endpass_sdk.presentation.utils.CookeManagerSingleton.COOKIES_HEADER
import java.net.HttpCookie



class ReceivedCookiesInterceptor : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {


        // Save cookies in CookieManager
        val response = chain.proceed(chain.request())
        val cookiesHeader = response.headers(COOKIES_HEADER)

        if (cookiesHeader.isNotEmpty()) {
            for (cookie in cookiesHeader) {
                CookeManagerSingleton.msCookieManager.cookieStore.add(null, HttpCookie.parse(cookie)[0])

            }
        }

        return response

    }


}
