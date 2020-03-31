package com.cpacm.comic.model.http

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.bean.*
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * <p>
 *     动漫之家api
 *     后缀 ?channel=Android&_debug=0&version=2.7.022&timestamp=1575613504
 * @author cpacm 2019-12-03
 */
interface DmzjApis {

    companion object {
        const val ORIGIN_URL = "https://www.dmzj.com/"
        val BASE_URL = "http://v3api.dmzj.com/"
        val DETAIL_URL = BASE_URL + "comic/comic_{id}.json"
    }

    /**
     * dmzj搜索接口
     */
    @GET("search/show/0/{keyword}/{page}.json")
    fun dmzjSearch(
        @Path("keyword") keyword: String,
        @Path("page") page: Int
    ): Observable<List<DmzjSearchBean>>


    /**
     * 获取dmzj漫画内容
     */
    @GET("comic/comic_{id}.json")
    fun getDmzjComic(@Path("id") comicId: String): Observable<DmzjDetailComicBean>

    /**
     * 获取dmzj章节内容
     */
    @GET("chapter/{id}/{chapterId}.json")
    fun getDmzjComicContent(
        @Path("id") id: String,
        @Path("chapterId") chapterId: String
    ): Observable<DmzjContentBean>


    /**
     * 获取dmzj最近更新
     * page:页码：从 0 开始
     */
    @GET("latest/100/{page}.json")
    fun getDmzjUpdateComic(@Path("page") page: Int): Observable<List<DmzjSimpleComicBean>>

    /**
     * dmzj 0/0/0/0->每日排行  0/1/0/0->每周排行 0/2/0/0->每月排行  0/3/0/0->总排行
     * 或许可以作为推荐使用
     */
    @GET("rank/0/{date}/0/{page}.json")
    fun getDmzjRankComic(
        @Path("date") date: Int,
        @Path("page") page: Int
    ): Observable<List<DmzjSimpleComicBean>>

    /**
     * 分类下漫画 第一个0表示为分类tagid 第二个0表示按人气，1表示按更新 第三个表示页数
     */
    @GET("classify/{category}/0/{page}.json")
    fun getDmzjClassifyComic(
        @Path("category") category: Int,
        @Path("page") page: Int
    ): Observable<List<DmzjSimpleComicBean>>

    /**
     * 获取所有分类
     */
    @GET("0/category.json")
    fun getDmzjCategory(): Observable<List<DmzjTagBean>>
}