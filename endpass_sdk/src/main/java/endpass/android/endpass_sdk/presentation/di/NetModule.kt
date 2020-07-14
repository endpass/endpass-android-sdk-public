package endpass.android.endpass_sdk.presentation.di

import android.content.Context
import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import endpass.android.endpass_sdk.data.remote.ApiService
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.utils.AddCookiesInterceptor
import endpass.android.endpass_sdk.presentation.utils.AuthInterceptor
import endpass.android.endpass_sdk.presentation.utils.ReceivedCookiesInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val netModule = module {
    factory {
        createOkHttpClient(get())
    }
    factory {
        createGson()
    }
    factory {
        createWebService<ApiService>(get(), Constant.BASE_URL, get())
    }
}


private fun createOkHttpClient(context: Context): OkHttpClient {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    return OkHttpClient.Builder()
        .connectTimeout(Constant.CONNECT_TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(Constant.READ_TIMEOUT, TimeUnit.SECONDS)
        .followRedirects(false)
        .followRedirects(false)
        .addInterceptor(AuthInterceptor(context))
        .addInterceptor(ReceivedCookiesInterceptor())
        .addInterceptor(AddCookiesInterceptor())
        .addInterceptor(httpLoggingInterceptor)
        .build()
}


val serialization = object : ExclusionStrategy {
    override fun shouldSkipClass(clazz: Class<*>?): Boolean {
        return false
    }

    override fun shouldSkipField(f: FieldAttributes?): Boolean {
        return f?.name?.contains("imgRes", true) ?: false
    }

}

fun createGson(): Gson {
    return GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        .setLenient()
        .addDeserializationExclusionStrategy(serialization)
        .create()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient, url: String, gson: Gson): T {
    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson)).build()
    return retrofit.create(T::class.java)
}


/**
 * Returns a trust manager that trusts `certificates` and none other. HTTPS services whose
 * certificates have not been signed by these certificates will fail with a `SSLHandshakeException`.
 *
 *
 * This can be used to replace the host platform's built-in trusted certificates with a custom
 * set. This is useful in development where certificate authority-trusted certificates aren't
 * available. Or in production, to avoid reliance on third-party certificate authorities.
 *
 *
 * See also [CertificatePinner], which can limit trusted certificates while still using
 * the host platform's built-in trust store.
 *
 * <h3>Warning: Customizing Trusted Certificates is Dangerous!</h3>
 *
 *
 * Relying on your own trusted certificates limits your server team's ability to update their
 * TLS certificates. By installing a specific set of trusted certificates, you take on additional
 * operational complexity and limit your ability to migrate between certificate authorities. Do
 * not use custom trusted certificates in production without the blessing of your server's TLS
 * administrator.
 */

