package com.cpacm.comic.ui.widgets

import android.app.Activity
import android.app.Dialog
import android.graphics.Point
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.cpacm.comic.R
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.ComicSettingManager.Companion.KEYWORD_COMIC_PICA_SERVER
import com.cpacm.comic.site.pica.PicaListViewModel

import com.cpacm.libarch.utils.MoeLogger
import kotlinx.android.synthetic.main.dialog_pica_login.*


/**
 *
 *
 * pica登录界面
 * created by cpacm at 2019/12/27.
 */

class PicaLoginDialog(private val activity: Activity, private val viewModel: PicaListViewModel) : Dialog(activity, R.style.FullHeightDialog) {

    init {
        setContentView(R.layout.dialog_pica_login)

        val dialogWindow = window
        val m = activity.windowManager
        val d = m.defaultDisplay // 获取屏幕宽、高用
        val point = Point()
        d.getSize(point)
        val p = dialogWindow!!.attributes // 获取对话框当前的参数值
        p.width = (point.x * 0.8).toInt() // 宽度设置为屏幕的0.8
        dialogWindow.attributes = p
        setCanceledOnTouchOutside(false)
        init()

        observeLivaData()
    }

    private fun observeLivaData() {
        viewModel.address.observe(activity as LifecycleOwner, Observer {
            initLayout.visibility = View.GONE
            addressLayout.visibility = View.VISIBLE
            loginLayout.visibility = View.GONE

            address1.visibility = View.VISIBLE
            address2.visibility = View.GONE
            address3.visibility = View.GONE
            address1.setOnClickListener {
                loginStep("")
                ComicSettingManager.getInstance().setSetting(KEYWORD_COMIC_PICA_SERVER, 1)
            }
            if (it.size >= 1) {
                address2.visibility = View.VISIBLE
                address2.setOnClickListener { _ ->
                    ComicSettingManager.getInstance().setSetting(KEYWORD_COMIC_PICA_SERVER, 2)
                    loginStep(it[0])
                }
            }
            if (it.size >= 2) {
                address3.visibility = View.VISIBLE
                address3.setOnClickListener { _ ->
                    ComicSettingManager.getInstance().setSetting(KEYWORD_COMIC_PICA_SERVER, 3)
                    loginStep(it[1])
                }
            }
            MoeLogger.v(it.toString())
        })

        viewModel.login.observe(activity as LifecycleOwner, Observer {
            if (it) {
                Toast.makeText(activity, R.string.comic_login_success, Toast.LENGTH_SHORT).show()
                finishStep()
            } else {
                Toast.makeText(activity, R.string.comic_login_fail, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loginStep(address: String) {
        val key = ComicSettingManager.KEYWORD_COMIC_AUTHORITY + "-" + address
        ComicSettingManager.getInstance().setSetting(ComicSettingManager.KEYWORD_COMIC_PICA_ADDRESS, address)
        val auth = ComicSettingManager.getInstance().getSetting(key)
        if (auth.isNotEmpty()) {
            finishStep()
        } else {
            initLayout.visibility = View.GONE
            addressLayout.visibility = View.GONE
            loginLayout.visibility = View.VISIBLE
        }
    }

    fun gotoLogin() {
        val address = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_COMIC_PICA_ADDRESS)
        val key = ComicSettingManager.KEYWORD_COMIC_AUTHORITY + "-" + address
        ComicSettingManager.getInstance().setSetting(key, "")
        loginStep(address)
    }

    private fun finishStep() {
        dismissStep()
        viewModel.initConfigAndRefresh()
        viewModel.refresh.postValue(true)
    }

    override fun dismiss() {
        super.dismiss()
        activity.finish()
    }

    fun dismissStep() {
        super.dismiss()
    }


    /**
     * 初始化控件
     */
    private fun init() {
        initLayout.visibility = View.VISIBLE
        addressLayout.visibility = View.GONE
        loginLayout.visibility = View.GONE

        picaLogin.setOnClickListener {
            val email = emailEt.text.toString().trim()
            if (email.isEmpty() || email.length < 4) {
                Toast.makeText(context, R.string.comic_login_email_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val password = passwordEt.text.toString().trim()
            if (password.isEmpty() || password.length < 6) {
                Toast.makeText(context, R.string.comic_login_password_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val address = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_COMIC_PICA_ADDRESS)
            viewModel.login(email, password, address)
        }


        viewModel.initAddress()
    }
}
