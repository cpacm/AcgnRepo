package com.cpacm.libarch.model.http

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author: cpacm
 * @date: 2016/8/22
 * @desciption: rxjava 简单事件分发器
 */
class RxBus {

    private val bus: PublishSubject<Any>

    // PublishSubject只会把在订阅发生的时间点之后来自原始Observable的数据发射给观察者
    init {
        bus = PublishSubject.create()
    }

    fun <T> toObservable(classType: Class<T>): Observable<T> {
        return bus.ofType(classType)
    }

    fun hasObservers(): Boolean {
        return bus.hasObservers()
    }

    // 发送一个新的事件
    fun post(o: Any) {
        bus.onNext(o)
    }

    companion object {
        @Volatile
        private var defaultInstance: RxBus? = null

        // 单例RxBus
        val default: RxBus?
            get() {
                if (defaultInstance == null) {
                    synchronized(RxBus::class.java) {
                        if (defaultInstance == null) {
                            defaultInstance = RxBus()
                        }
                    }
                }
                return defaultInstance
            }
    }

}
