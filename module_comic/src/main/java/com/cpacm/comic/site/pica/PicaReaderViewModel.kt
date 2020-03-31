package com.cpacm.comic.site.pica

import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.bean.ComicContentBean
import com.cpacm.comic.model.http.PicaRetrofitHelper
import com.cpacm.comic.ui.ComicReaderViewModel

/**
 * <p>
 *
 * @author cpacm 2019-12-19
 */
class PicaReaderViewModel : ComicReaderViewModel() {

    override fun getContentData(index: Int, order: Int, immediately: Boolean, offsetPage: Int) {
        if (index == -1) return //已超出章节目录
        withUI {
            val chapterBean = chapterData[index]
            val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
            val imgList = withRequest {
                val imgList = arrayListOf<String>()
                var epLoop = true
                var page = 1
                while (epLoop) {
                    val response = PicaRetrofitHelper.instance.picaApi.getComicContent(comicId, chapterBean.chapterId!!, page)
                    if (response.code == 200) {
                        val doc = response.data!!.pages
                        epLoop = doc.page < doc.pages
                        page += 1
                        for (content in doc.docs) {
                            imgList.add(content.getCoverThumb(imageServer))
                        }
                    } else {
                        epLoop = false
                    }
                }
                imgList
            }
            val comicContentBean = ComicContentBean()
            comicContentBean.title = chapterData[index].chapterTitle
            comicContentBean.picNum = imgList.size
            comicContentBean.comicId = comicId
            comicContentBean.chapterId = chapterData[index].chapterId ?: ""
            comicContentBean.pageUrl = imgList
            loadData(index, order, comicContentBean, offsetPage)
        }
    }
}