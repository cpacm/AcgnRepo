package com.cpacm.comic.site.pica

import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.bean.SortBean
import com.cpacm.comic.model.http.PicaRetrofitHelper
import com.cpacm.comic.ui.search.ComicSearchViewModel

/**
 *
 * <p>
 *     漫画搜索
 * @author cpacm 2019/12/20
 */
open class PicaSearchViewModel : ComicSearchViewModel() {

    override fun searchRequest(keyword: String, type: Int) {
        if (page == 0) page = 1
        when (type) {
            0 -> searchAdvanced(keyword)
            1 -> searchCategory(keyword)
            2 -> searchTag(keyword)
            3 -> searchAdvanced(keyword)
            else -> searchAdvanced(keyword)
        }
    }

    fun searchAdvanced(keyword: String) {
        withUI {
            val comics = withRequestOrNull {
                val response = PicaRetrofitHelper.instance.picaApi.search(SortBean(keyword, "dd"), page)
                if (response.code == 200) {
                    response.data?.comics
                } else null
            }
            val list = results.value ?: arrayListOf()
            var has = false
            val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
            if (comics != null) {
                val comicList = comics.docs
                for (pica in comicList) {
                    list.add(pica.parse(imageServer))
                }
                has = comics.page < comics.pages
            }
            results.postValue(list)
            hasMore.postValue(has)
            page += 1
        }
    }

    fun searchCategory(keyword: String) {
        withUI {
            val comics = withRequestOrNull {
                val response = PicaRetrofitHelper.instance.picaApi.getCategoryList(page, keyword)
                if (response.code == 200) {
                    response.data?.comics
                } else null
            }
            val list = results.value ?: arrayListOf()
            var has = false
            val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
            if (comics != null) {
                val comicList = comics.docs
                for (pica in comicList) {
                    list.add(pica.parse(imageServer))
                }
                has = comics.page < comics.pages
            }
            results.postValue(list)
            hasMore.postValue(has)
            page += 1
        }
    }

    fun searchTag(keyword: String) {
        withUI {
            val comics = withRequestOrNull {
                val response = PicaRetrofitHelper.instance.picaApi.getTagList(page, keyword)
                if (response.code == 200) {
                    response.data?.comics
                } else null
            }
            val list = results.value ?: arrayListOf()
            var has = false
            val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
            if (comics != null) {
                val comicList = comics.docs
                for (pica in comicList) {
                    list.add(pica.parse(imageServer))
                }
                has = comics.page < comics.pages
            }
            results.postValue(list)
            hasMore.postValue(has)
            page += 1
        }
    }

}