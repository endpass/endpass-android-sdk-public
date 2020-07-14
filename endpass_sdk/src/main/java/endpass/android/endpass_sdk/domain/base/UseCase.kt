package endpass.android.endpass_sdk.domain.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase {

    private var isAttached = false
    protected var lastDisposable: Disposable? = null
    var disposables: CompositeDisposable = CompositeDisposable()



    protected fun disposeLatest() {
        lastDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    fun attachToLifecycle() {
        isAttached = true
    }

    fun dispose() {
        disposables.clear()
    }

    fun isAttachedToLifecycle() = isAttached
}