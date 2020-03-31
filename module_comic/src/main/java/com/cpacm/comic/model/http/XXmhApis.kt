package com.cpacm.comic.model.http

import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * <p>
 *     漫画堆api
 * @author cpacm 2019-12-20
 */
interface XXmhApis {

    companion object {
        val BASE_URL = "https://www.177mh.net/"
        val ORIGIN_URL = BASE_URL
        val DETAIL_URL = BASE_URL + "colist_{id}.html"
    }

    /**
     * 获取最近更新
     * page:页码：从 1 开始
     */
    @GET("index.html")
    suspend fun getRecommend(): ResponseBody

    /**
     *
     * 可作为推荐使用
     */
    @GET("{extra}")
    suspend fun getXXRankComic(@Path("extra") url: String): ResponseBody

    /**
     * 分类
     * https://www.177mh.net/rexue/index_0.html
     */
    @GET("{category}/index_{page}.html")
    suspend fun getXXCategoryComic(
        @Path("category") category: String,
        @Path("page") page: Int
    ): ResponseBody

    /**
     * 获取漫画内容
     */
    @GET("colist_{id}.html")
    suspend fun getXXComic(@Path("id") comicId: String): ResponseBody

    /**
     * 获取章节内容
     * https://www.177mh.net/201607/337415.html
     */
    @GET("{address}.html")
    suspend fun getXXComicContent(@Path("address") address: String): ResponseBody


    /**
     * 搜索接口
     * https://so.177mh.net/k.php?k=%E5%A4%A9%E6%89%8D%E9%BA%BB%E5%B0%86
     */
    @GET("https://so.177mh.net/k.php")
    suspend fun xxSearch(@Query("k") keyword: String, @Query("p") page: Int): ResponseBody

    /**
     * 搜索接口
     * https://so.177mh.net/k.php?word=%E5%A4%A9%E6%89%8D%E9%BA%BB%E5%B0%86
     */
    @GET("https://so.177mh.net/k.php")
    suspend fun xxSearchWord(@Query("word") keyword: String, @Query("p") page: Int): ResponseBody

    /**
     * 获取图片服务器地址
     */
    @GET("https://css.gdbyhtl.net/img_v1/hwcf_svr.aspx")
    suspend fun xxServerList(
        @Query("z") atsvr: String,
        @Query("s") s: String,
        @Query("cid") comicId: String,
        @Query("coid") coid: String
    ): ResponseBody

}