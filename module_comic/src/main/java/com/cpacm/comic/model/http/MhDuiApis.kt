package com.cpacm.comic.model.http

import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * <p>
 *     漫画堆api
 * @author cpacm 2019-12-20
 */
interface MhDuiApis {

    companion object {
        val BASE_URL = "https://www.manhuadui.com/"
        val ORIGIN_URL = BASE_URL
        const val BASE_URL2 = "https://www.manhuadui.com"
        val DETAIL_URL = BASE_URL + "manhua/{id}/"
    }

    /**
     * 获取最近更新
     * page:页码：从 1 开始
     */
    @GET("list/riben/update/{page}/")
    suspend fun getMHDuiUpdateComic(@Path("page") page: Int): ResponseBody

    /**
     * 排行榜
     * 或许可以作为推荐使用
     */
    @GET("rank/{type}/")
    suspend fun getMHDuiRankComic(@Path("type") type: String): ResponseBody

    /**
     * 分类
     */
    @GET("{category}/{page}/")
    suspend fun getMHDuiCategoryComic(
        @Path("category") category: String,
        @Path("page") page: Int
    ): ResponseBody

    /**
     * 获取漫画内容
     */
    @GET("manhua/{id}/")
    suspend fun getMHDuiComic(@Path("id") comicId: String): ResponseBody

    /**
     * 获取章节内容
     * https://www.manhuadui.com/manhua/shuchonggongzhu/430399.html
     */
    @GET("/manhua/{id}/{chapterId}.html")
    suspend fun getHHComicContent(
        @Path("id") id: String,
        @Path("chapterId") chapterId: String
    ): ResponseBody


    /**
     * 搜索接口
     * https://www.manhuadui.com/search/?keywords=%E7%8C%8E%E4%BA%BA&page=2
     */
    @GET("search/")
    suspend fun mhDuiSearch(
        @Query("keywords") keyword: String,
        @Query("page") page: Int
    ): ResponseBody


}