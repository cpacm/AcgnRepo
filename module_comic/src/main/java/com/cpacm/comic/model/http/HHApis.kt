package com.cpacm.comic.model.http

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

/**
 * <p>
 *     汗汗漫画api
 *     后缀 http://ddmmcc.com/dfcomiclist_1.htm
 * @author cpacm 2019-12-03
 */
interface HHApis {

    companion object {
        val BASE_URL = "http://www.hhimm.com/"
        val BASE_URL2 = BASE_URL.substring(0, BASE_URL.length - 1)
        val ORIGIN_URL = BASE_URL + "comic"
        val DETAIL_URL = BASE_URL + "comic/18{id}/"
    }

    /**
     * 获取最近更新
     * http://www.hhimm.com/top/newrating.aspx
     * page:页码：从 1 开始
     */
    @GET("top/newrating.aspx")
    suspend fun getHHUpdateComic(): ResponseBody

    /**
     * 排行榜
     * 或许可以作为推荐使用
     * http://www.hhimm.com/top/newrating.aspx
     */
    @GET("top/{type}.aspx")
    suspend fun getHHRankComic(@Path("type") type: String): ResponseBody

    /**
     * 分类下漫画 第一个1表示分类 第二个表示页数
     * http://www.hhimm.com/comic/class_2.html
     */
    @GET("comic/class_{category}/{page}.html")
    suspend fun getHHCategoryComic(
        @Path("category") category: String,
        @Path("page") page: Int
    ): ResponseBody

    /**
     * 获取hh漫画内容
     * http://www.hhimm.com/manhua/37477.html
     * http://ddmmcc.com/comic/18474/
     */
    @GET("manhua/{id}.html")
    suspend fun getHHComic(@Path("id") comicId: String): ResponseBody

    /**
     * 获取hh图片域名列表
     * http://ddmmcc.com/vols/474_4560/ 图片所有
     */
    @GET("js/ds.js")
    suspend fun getHHDomainList(): ResponseBody

    /**
     * 获取hh章节内容
     * http://www.hhimm.com/cool376562/1.html?s=12
     */
    @GET("cool{chapterId}/{page}.html")
    suspend fun getHHComicContent(
        @Path("chapterId") chapterId: String,
        @Path("page") page: Int,
        @Query("s") server: String
    ): ResponseBody

    /**
     * 获取请求的头部信息
     */
    @HEAD
    suspend fun head(@Url url: String): Response<Void>


    /**
     * hh搜索接口
     */
    @GET("comicsearch/s.aspx")
    suspend fun hhSearch(@Query("s") keyword: String): ResponseBody


}