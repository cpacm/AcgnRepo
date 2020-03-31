package com.cpacm.comic.ui.site

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.cpacm.comic.ComicApp
import com.cpacm.comic.R
import com.cpacm.comic.site.ComicSiteFactory
import com.cpacm.comic.site.dmzj.DmzjSite
import com.cpacm.comic.site.hhcomic.HHSite
import com.cpacm.comic.site.manhuadui.MhDuiSite
import com.cpacm.comic.site.mgbz.MgbzSite
import com.cpacm.comic.site.mh123.Mh123Site
import com.cpacm.comic.site.pica.PicaSite
import com.cpacm.comic.site.xxmh.XXmhSite
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_comic_site.*

/**
 * <p>
 *     漫画源
 * @author cpacm 2019-12-05
 */
class ComicSiteFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.ComicBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_comic_site, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComicSites()
    }


    private fun initComicSites() {
        val comicAdapter = ComicSiteAdapter(context!!)

        recycleView.layoutManager = GridLayoutManager(context, 2)
        recycleView.adapter = comicAdapter

        val data = arrayListOf(
            DmzjSite(), //动漫之家
            MhDuiSite(), //漫画堆
            Mh123Site(), //漫画123
            //PicaSite(), //pica漫画
            MgbzSite(), //mangabz -> http://www.mangabz.com/
            XXmhSite(),//新新漫画
            HHSite() //汗汗动漫
        )

        comicAdapter.setData(data)
        comicAdapter.comicSiteListener = object : ComicSiteAdapter.OnComicSiteListener {
            override fun onComicSiteClick(site: ComicSiteFactory) {
                ComicApp.getInstance().comicSite = site
                startActivity(Intent(context, ComicListActivity::class.java))
            }
        }

    }


}