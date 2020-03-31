package com.cpacm.libarch

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.cpacm.libarch.utils.MoeLogger
import java.util.HashSet

/**
 * @author cpacm 2019/1/3
 */
class ArchApp private constructor(application: Application) {

    private val context: Context

    /**
     * 运用set来保存每一个activity
     * activity set
     */
    private val activities: HashSet<Class<*>> = HashSet()

    init {
        context = application

        if (isMainProcess(this.context)) {
            registerActivityLifecycle(application)
        }
    }

    /**
     * 监听所有Activity的生命周期，注册友盟埋点
     */
    fun registerActivityLifecycle(app: Application) {
        app.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {
            }

            override fun onActivityResumed(activity: Activity?) {
            }

            override fun onActivityStarted(activity: Activity?) {}

            override fun onActivityDestroyed(activity: Activity?) {
                activities.remove(activity!!.javaClass)//从列表中删除一个activity
            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityStopped(activity: Activity?) {}

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                activities.add(activity!!.javaClass)//添加一个activity到列表中
            }
        })

    }

    /**
     * 主要用来判断主页是否在运行
     *
     * @param activity
     * @return
     */
    fun isActivityRunning(activity: Class<*>): Boolean {
        return activities.contains(activity)
    }

    /**
     * 关闭list内的每一个activity并且退出应用<br></br>
     * close all activity and exit app
     */
    fun exit() {
        activities.clear()
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(0)
    }

    fun getContext(): Context {
        return context
    }

    /**
     * 获取当前进程名
     */
    private fun getCurrentProcessName(context: Context): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }

    /**
     * 判断是否主进程
     *
     * @return
     */
    fun isMainProcess(context: Context): Boolean {
        return context.packageName == getCurrentProcessName(context)
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: ArchApp? = null

        fun getInstance(): ArchApp {
            if (instance == null) throw NullPointerException("please register Arch module before use!!")
            return instance!!
        }

        /**
         * 必须在Application onCreate中调用，以便初始化本模块。
         */
        fun register(application: Application) {
            instance = ArchApp(application)
        }
    }
}