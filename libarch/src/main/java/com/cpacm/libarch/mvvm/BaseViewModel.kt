package com.cpacm.libarch.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cpacm.libarch.model.http.ApiException
import com.cpacm.libarch.utils.MoeLogger
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.*

/**
 * <p>
 *     兼容rxjava 和 kotlin coroutine
 * created by cpacm at 2019/4/7.
 */
open class BaseViewModel : ViewModel(), CoroutineScope by MainScope() {

    var compositeDisposable: CompositeDisposable? = null

    val error = MutableLiveData<Throwable>()

    val mainScope = MainScope()

    override fun onCleared() {
        super.onCleared()
        unDisposable()
        cancel()
    }

    fun withUIAndError(block: suspend CoroutineScope.() -> Unit, error: (e: Exception) -> Unit) {
        mainScope.launch {
            try {
                block()
            } catch (e: Exception) {
                MoeLogger.e(e.message)
                error(ApiException(e.message ?: "出现错误，请重试"))
            }
        }
    }

    fun withUI(block: suspend CoroutineScope.() -> Unit) {
        mainScope.launch {
            try {
                block()
            } catch (e: Exception) {
                MoeLogger.e(e.message)
                error.postValue(ApiException(e.message ?: "出现错误，请重试"))
            }
        }
    }

    suspend fun <T : Any> withRequest(call: suspend () -> T): T {
        return withContext(Dispatchers.IO) { call.invoke() }
    }

    suspend fun <T : Any> withRequestOrNull(call: suspend () -> T?): T? {
        return withContext(Dispatchers.IO) { call.invoke() }
    }

    protected fun unDisposable() {
        if (compositeDisposable != null) {
            compositeDisposable!!.clear()
        }
    }

    fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }
}