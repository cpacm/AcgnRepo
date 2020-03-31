package com.cpacm.libarch.mvvm

import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.cpacm.libarch.R
import com.cpacm.libarch.model.http.ApiException
import com.cpacm.libarch.utils.MoeLogger
import java.lang.Exception

/**
 *
 * MVVM 模式下的Activity
 *
 * @author cpacm 2018/1/16
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    abstract val viewModelClass: Class<out T>

    protected val viewModel: T by unsafeLazy {
        ViewModelProvider(this).get(viewModelClass)
    }

    fun <K> unsafeLazy(initializer: () -> K) = lazy(LazyThreadSafetyMode.NONE, initializer)

    private fun observeErrorData() {
        viewModel.error.observe(this, Observer {
            it?.let {
                MoeLogger.e(it.toString())
                onErrorCallback(it)
            }
        })
    }

    open fun onErrorCallback(it: Throwable?) {
        Toast.makeText(this,
                if (it is ApiException) it.message.toString() else getString(R.string.network_error),
                Toast.LENGTH_SHORT)
                .show()
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        observeLiveData()
        observeErrorData()
    }


    /**
     * 建立数据与ViewModel的联系
     */
    abstract fun observeLiveData()


    fun showSnackBar(view: View, toast: String) {
        Snackbar.make(view, toast, Snackbar.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, @StringRes stringRes: Int, callback: Snackbar.Callback) {
        val snackbar = Snackbar.make(view, stringRes, Snackbar.LENGTH_SHORT)
        snackbar.addCallback(callback)
        snackbar.show()
    }

    fun showSnackBar(view: View, stringRes: String, callback: Snackbar.Callback) {
        val snackbar = Snackbar.make(view, stringRes, Snackbar.LENGTH_SHORT)
        snackbar.addCallback(callback)
        snackbar.show()
    }

    fun showSnackBar(view: View, @StringRes toast: Int) {
        Snackbar.make(view, toast, Snackbar.LENGTH_SHORT).show()
    }

    fun showToast(@StringRes toast: Int) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    fun showToast(toast: String) {
        Toast.makeText(this, toast, Toast.LENGTH_SHORT).show()
    }

    fun startActivity(activity: Class<*>) {
        val i = Intent()
        i.setClass(this, activity)
        startActivity(i)
    }

}