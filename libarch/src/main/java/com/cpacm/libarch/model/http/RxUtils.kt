package com.cpacm.libarch.model.http

import com.cpacm.libarch.model.ApiResponse
import io.reactivex.FlowableTransformer

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author: cpacm
 * @date: 2017/2/17
 * @desciption:
 */

object RxUtils {

    /**
     * 子线程运行，主线程回调
     *
     * @param <T>
     * @return
    </T> */
    fun <T> io_main(): ObservableTransformer<T, T> {
        return ObservableTransformer { upstream ->
            upstream.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    /**
     * Flowable线程切换简化
     */
    fun <T> rxSchedulerHelper(): FlowableTransformer<T, T> {    //compose简化线程
        return FlowableTransformer { observable ->
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }


    /**
     * 生成Observable
     *
     * @param <T>
     * @return
    </T> */
    fun <T> createData(t: T): Observable<T> {
        return Observable.create { e ->
            e.onNext(t)
            e.onComplete()
        }
    }

}
