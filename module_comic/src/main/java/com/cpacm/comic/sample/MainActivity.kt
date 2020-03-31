package com.cpacm.comic.sample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cpacm.comic.R
import com.cpacm.comic.ui.site.ComicSiteFragment
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragment: ComicSiteFragment? = null
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        go.setOnClickListener {
            if (fragment == null) {
                fragment = ComicSiteFragment()
            }
            if (fragment?.isAdded == false) {
                fragment!!.show(supportFragmentManager, "bottomcomic")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
