package endpass.android.endpass_sdk.presentation.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import android.net.ConnectivityManager
import endpass.android.endpass_sdk.R


/**
 * Класс для передачи дефолтных Header параметров
 * Добавляеться автоматический в каждый запрос
 */


class AuthInterceptor(var context: Context) : Interceptor {

    companion object {
        const val AUTHORIZATION_TOKEN = "Authorization"
        const val TOKEN_PREFIX = "Bearer "

    }

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val localData = LocalData(context)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline()) throw NoConnectivityException()


        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader(AUTHORIZATION_TOKEN, TOKEN_PREFIX + localData.accessToken)
            .build()


        return chain.proceed(newRequest)

    }

    private fun isOnline(): Boolean {
        val netInfo = connectivityManager.activeNetworkInfo
        return netInfo != null && netInfo.isConnected
    }

    inner class NoConnectivityException : Throwable() {

        override val message: String?
            get() = context.getString(R.string.error_no_internet)

    }

}
