package com.cpacm.comic.model.http

import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * <p>
 *     漫画堆api
 * @author cpacm 2019-12-20
 */
interface Mh123Apis {

    companion object {
        val BASE_URL = "https://www.manhua123.net/"
        val ORIGIN_URL = BASE_URL
        val DETAIL_URL = BASE_URL + "comic/{id}.html"
    }

    /**
     * 获取最近更新
     * page:页码：从 1 开始
     * https://www.manhua123.net/page/new/1.html
     */
    @GET("page/new/{page}.html")
    suspend fun getUpdateComic(@Path("page") page: Int): ResponseBody

    /**
     * 排行榜
     */
    @GET("{rank}/{page}.html")
    suspend fun getRankComic(@Path("rank") url: String, @Path("page") page: Int): ResponseBody

    /**
     * 分类
     * https://www.manhua123.net/list/shaonianrexue/
     */
    @GET("list/shaonianrexue/")
    suspend fun loadCategoryComic(): ResponseBody

    /**
     * 分类内容
     * https://www.manhua123.net/list/shaonianrexue/
     */
    @GET("tags/{category}/{page}.html")
    suspend fun getCategoryComic(
        @Path("category") category: String,
        @Path("page") page: Int
    ): ResponseBody

    /**
     * 获取漫画内容
     */
    @GET("comic/{id}.html")
    suspend fun getComicDetail(@Path("id") comicId: String): ResponseBody

    /**
     * 获取章节内容
     */
    @GET("comic/{comicId}/{chapterId}.html")
    suspend fun getComicContent(
        @Path("comicId") comicId: String,
        @Path("chapterId") chapterId: String
    ): ResponseBody

    /**
     * 搜索接口
     * https://www.manhua123.net/index.php?m=vod-search-pg-2-wd-%E7%88%B1.html
     */
    @GET("index.php")
    suspend fun searchWord(@Query("m") word: String): ResponseBody
}