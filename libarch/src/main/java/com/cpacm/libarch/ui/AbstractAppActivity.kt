package com.cpacm.libarch.ui

import android.os.Build
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.TextView
import com.cpacm.libarch.mvvm.BaseActivity
import com.cpacm.libarch.mvvm.BaseViewModel

/**
 *
 * <p>
 *     app下的共用的Activity
 * @author cpacm 2018/1/16
 */
abstract class AbstractAppActivity<T : BaseViewModel> : BaseActivity<T>() {

    protected fun setToolBar(toolbar: Toolbar, title: String) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
    }

    /**
     * 当使用 #{R.layout.activity_common_toolbar} 布局时可以调用该方法进行初始化
     * @param titleId
     */
    protected fun initToolBar(@StringRes titleId: Int) {
        initToolBar(getString(titleId))
    }

    protected fun initToolBar(title: String) {
        val toolbar = findViewById<View>(com.cpacm.libarch.R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        val titleTv = findViewById<TextView>(com.cpacm.libarch.R.id.title)
        titleTv.text = title
    }
}