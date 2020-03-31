package com.cpacm.acgnrepo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.cpacm.comic.ui.site.ComicSiteFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var comicSiteFragment: ComicSiteFragment? = null
    //var novelSiteFragment: NovelSiteFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        comic.setOnClickListener {
            if (comicSiteFragment == null) {
                comicSiteFragment = ComicSiteFragment()
            }
            if (!comicSiteFragment!!.isAdded) {
                comicSiteFragment?.show(supportFragmentManager, "bottomcomic")
            }
        }

        novel.setOnClickListener {
            Toast.makeText(this, "正在上传中", Toast.LENGTH_SHORT).show()
//            if (novelSiteFragment == null) {
//                novelSiteFragment = NovelSiteFragment()
//            }
//            if (!novelSiteFragment!!.isAdded) {
//                novelSiteFragment?.show(supportFragmentManager, "bottomnovel")
//            }
        }
    }
}
