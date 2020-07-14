package endpass.android.endpass_sdk.data.exception

import endpass.android.endpass_sdk.domain.exception.BusinessLogicException
import endpass.android.endpass_sdk.domain.exception.ServerException
import io.reactivex.Single
import retrofit2.Response


fun <T> request(call: Single<Response<T>>): Single<T> {
    return call.flatMap { response ->
        if (response.isSuccessful) {
            Single.just(response.body())
        } else {
            Single.error<T>(ServerException("Произошла непредвиденная ошибка...", response.code()))
        }
    }
}

fun <T> requestRedirect(call: Single<Response<String>>): Single<String> {
    return call.flatMap { response ->
        if (response.isSuccessful) {
            val url = response.raw().request().url().toString()
            Single.just(url)
        } else {

            if (response.code() == 302) {
                val redirectUrl = response.headers().get("Location")
                Single.just<String>(
                    redirectUrl
                )
            } else {
                Single.error<String>(
                    ServerException(
                        "Произошла непредвиденная ошибка...",
                        response.code()
                    )
                )
            }
        }
    }
}


fun handleError(error: Throwable, callback: (String, Int?) -> Unit) {
    when (error) {
        is BusinessLogicException -> callback.invoke(error.title, null)
        is ServerException -> callback.invoke(error.errorMessage, error.code)
        else -> callback("Ooops..", null)
    }
}