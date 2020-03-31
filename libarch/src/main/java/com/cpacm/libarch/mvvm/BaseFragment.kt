package com.cpacm.libarch.mvvm

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cpacm.libarch.R
import com.cpacm.libarch.model.http.ApiException
import com.cpacm.libarch.utils.MoeLogger
import com.cpacm.libarch.utils.SystemParamsUtils


/**
 * @Auther: cpacm
 * @Date: 2016/7/9.
 * @description: 基类Fragment
 */
abstract class BaseFragment<T : BaseViewModel> : Fragment() {

    abstract val viewModelClass: Class<out T>

    protected val viewModel: T by unsafeLazy { generateViewModel() }

    open fun generateViewModel(): T {
        return ViewModelProvider(this).get(viewModelClass)
    }


    fun <K> unsafeLazy(initializer: () -> K) = lazy(LazyThreadSafetyMode.NONE, initializer)

    fun observeErrorData() {
        viewModel.error.observe(this, Observer {
            it?.let {
                MoeLogger.e(it.toString())
                onErrorCallback(it)
            }
        })
    }

    open fun onErrorCallback(it: Throwable?) {
        if (activity != null) {
            Toast.makeText(activity,
                    if (it is ApiException) it.message.toString() else getString(R.string.network_error),
                    Toast.LENGTH_SHORT)
                    .show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeLiveData()
        observeErrorData()
    }

    abstract fun observeLiveData()

    fun findViewById(id: Int): View? {
        return view?.findViewById(id)
    }

    /**
     * 当使用 #{R.layout.activity_common_toolbar} 布局时可以调用该方法进行初始化
     *
     * @param titleId
     */
    protected fun initToolBar(toolbar: Toolbar, @StringRes titleId: Int) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayShowTitleEnabled(false)
        val titleTv = view!!.findViewById<View>(R.id.title) as TextView
        titleTv.setText(titleId)
    }

    /**
     * 在 Android 4.4 版本下无法显示内容在 StatusBar 下，所以无需修正 Toolbar 的位置
     *
     * @param view
     */
    protected fun fixToolbar(view: View) {
        val statusHeight = SystemParamsUtils.getStatusBarHeight(activity!!)
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, statusHeight, 0, 0)
    }

    protected fun fixAppBar(appBarLayout: AppBarLayout) {
        val minHeight = appBarLayout.minimumHeight
        val statusHeight = SystemParamsUtils.getStatusBarHeight(activity!!)
        val layoutParams = appBarLayout.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, statusHeight, 0, 0)
        appBarLayout.minimumHeight = minHeight + statusHeight

    }

    protected fun fixStatusBar(view: View, parentView: View) {
        val statusHeight = SystemParamsUtils.getStatusBarHeight(activity!!)
        val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams.setMargins(0, statusHeight, 0, 0)

        val layoutParams2 = parentView.layoutParams as ViewGroup.MarginLayoutParams
        layoutParams2.setMargins(0, 0, 0, statusHeight)

    }

}
