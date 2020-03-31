package com.cpacm.comic.site.pica

import android.app.Activity
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.cpacm.comic.R
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.ComicSettingManager.Companion.KEYWORD_COMIC_PICA_ADDRESS_SET
import com.cpacm.comic.model.bean.*
import com.cpacm.comic.model.http.PicaRetrofitHelper
import com.cpacm.comic.ui.site.ComicListViewModel
import com.cpacm.comic.ui.widgets.PicaLoginDialog
import com.cpacm.libarch.model.http.ApiException
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * <p>
 *     pica数据列表
 * @author cpacm 2019-12-06
 */
class PicaListViewModel : ComicListViewModel() {

    val address = MutableLiveData<MutableList<String>>()
    val login = MutableLiveData<Boolean>()
    var loginDialog: PicaLoginDialog? = null

    var extra: String = "H24"
    var curPage = 1

    init {
    }

    override fun setType(type: ComicDataType, extra: String) {
        this.dataType = type
        this.extra = extra
    }

    override fun needLogin(context: Context, reLogin: Boolean): Boolean {
        if (loginDialog == null) {
            loginDialog = PicaLoginDialog(context as Activity, this)
        }
        loginDialog?.show()
        if (reLogin) {
            loginDialog?.gotoLogin()
        }
        return true
    }

    fun initAddress(retry: Int = 2) {
        withUIAndError({
            val picaAddress = withRequest {
                if (retry == 2) {
                    PicaRetrofitHelper.instance.picaAddressApi.getApiAddress()
                } else {
                    PicaRetrofitHelper.instance.picaAddressApi.getApiAddress2()
                }
            }
            if (picaAddress.status == "ok") {
                val list = address.value ?: arrayListOf()
                list.addAll(picaAddress.addresses ?: arrayListOf())
                if (list.isNotEmpty()) {
                    ComicSettingManager.getInstance().setSetting(KEYWORD_COMIC_PICA_ADDRESS_SET, list.toMutableSet())
                }
                address.postValue(list)
            } else {
                throw ApiException("哔咔服务器请求失败")
            }
        }, {
            if (retry > 0) {
                initAddress(retry - 1)
            } else {
                val set = ComicSettingManager.getInstance().getSetting(KEYWORD_COMIC_PICA_ADDRESS_SET, mutableSetOf())
                if (set.isNotEmpty()) {
                    address.postValue(set.toMutableList())
                }
            }
        })
    }

    fun login(email: String, password: String, address: String) {
        withUIAndError({
            val token = withRequest {
                val param = JSONObject()
                param.put("email", email)
                param.put("password", password)
                val response = PicaRetrofitHelper.instance.picaApi.login(param.toString().toRequestBody(null))
                var token = ""
                if (response.code == 200) {
                    token = response.data?.token ?: ""
                }
                token
            }
            if (token.isEmpty()) {
                login.postValue(false)
                return@withUIAndError
            }
            val key = ComicSettingManager.KEYWORD_COMIC_AUTHORITY + "-" + address
            ComicSettingManager.getInstance().setSetting(key, token)
            login.postValue(true)
        }, { login.postValue(false) })
    }

    fun initConfigAndRefresh() {
        withUI {
            withRequest {
                val initialBean = PicaRetrofitHelper.instance.picaApi.initConfig()
                val imageServer = initialBean.data?.imageServer ?: ""
                ComicSettingManager.getInstance().setSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER, imageServer)
                val categories = initialBean.data?.categories ?: arrayListOf()
                categoryList.clear()
                for (c in categories) {
                    categoryList.add(ComicMenuBean(c.title ?: "未知", c.title ?: "未知".trim()))
                }
            }
            loadCategory()
            refresh()
        }
    }

    override fun refresh() {
        when (dataType) {
            ComicDataType.DATA_RECENTLY -> refreshUpdateList()
            ComicDataType.DATA_RANK -> refreshRankList()
            ComicDataType.DATA_CATEGORY -> refreshCategory()
        }

    }

    override fun loadMore() {
        when (dataType) {
            ComicDataType.DATA_RECENTLY -> getUpdateList()
            ComicDataType.DATA_RANK -> getRankList()
            ComicDataType.DATA_CATEGORY -> getCategoryList()
        }
    }

    private fun refreshUpdateList() {
        curPage = 1
        comicList.value?.clear()
        getUpdateList()
    }

    private fun getUpdateList() {
        withUI {
            withRequest {
                val response = PicaRetrofitHelper.instance.picaApi.recentlyUpdate(curPage)
                if (response.code == 200) {
                    val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
                    val doc = response.data!!.comics
                    val list = comicList.value ?: arrayListOf()
                    for (comic in doc.docs) {
                        list.add(comic.parse(imageServer))
                    }
                    title.postValue("最近更新")
                    comicList.postValue(list)
                    hasMore.postValue(doc.page < doc.pages)
                    curPage += 1
                } else {
                    error.postValue(ApiException("网络错误"))
                }
            }
        }
    }

    private fun refreshRankList() {
        curPage = 1
        comicList.value?.clear()
        getRankList()
    }

    private fun getRankList() {
        withUI {
            withRequest {
                val response = PicaRetrofitHelper.instance.picaApi.rankComic(extra)
                if (response.code == 200) {
                    val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
                    val doc = response.data!!.comics
                    val list = comicList.value ?: arrayListOf()
                    for (comic in doc) {
                        list.add(comic.parse(imageServer))
                    }
                    comicList.postValue(list)
                    hasMore.postValue(false)
                } else {
                    error.postValue(ApiException("网络错误"))
                }
            }
        }
    }

    private fun refreshCategory() {
        curPage = 1
        comicList.value?.clear()
        getCategoryList()
    }

    private fun getCategoryList() {
        withUI {
            withRequest {
                val response = PicaRetrofitHelper.instance.picaApi.getCategoryList(curPage, extra)
                if (response.code == 200) {
                    val imageServer = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_PICA_IMAGE_SERVER)
                    val doc = response.data!!.comics
                    val list = comicList.value ?: arrayListOf()
                    for (comic in doc.docs) {
                        list.add(comic.parse(imageServer))
                    }
                    comicList.postValue(list)
                    hasMore.postValue(doc.page < doc.pages)
                    curPage += 1
                } else {
                    error.postValue(ApiException("网络错误"))
                }
            }
        }
    }

    private fun loadCategory() {
        withUI {
            withRequest {
                val response = PicaRetrofitHelper.instance.picaApi.getCategory()
                if (response.code == 200) {
                    val doc = response.data!!.categories
                    categoryList.clear()
                    for (category in doc) {
                        if (category.isWeb == false) {
                            categoryList.add(ComicMenuBean(category.title ?: "未知", category.title
                                    ?: "未知".trim()))
                        }
                    }
                }
            }
        }
    }

    override fun getRankMenu(): List<ComicMenuBean> {
        val list = arrayListOf<ComicMenuBean>()
        list.add(ComicMenuBean("过去24小时", "H24"))
        list.add(ComicMenuBean("过去7天", "D7"))
        list.add(ComicMenuBean("过去30天", "D30"))
        return list
    }

    override fun getLogo(): Int {
        return R.drawable.ic_comic_pica
    }
}