package com.cpacm.comic.model.http

import okhttp3.ResponseBody
import retrofit2.http.*

/**
 * <p>
 *     漫画堆api
 * @author cpacm 2019-12-20
 */
interface MgbzApis {

    companion object {
        //http://www.mangabz.com/manga-list/
        val BASE_URL = "http://www.mangabz.com/"
        val ORIGIN_URL = BASE_URL
        const val BASE_URL2 = "http://www.mangabz.com"
        val DETAIL_URL = BASE_URL + "{id}/"
    }

    /**
     * 题材，状态，排序
     * http://www.mangabz.com/manga-list/ 全部，全部，人气
     * http://www.mangabz.com/manga-list-0-0-2/ 全部，全部，更新排序
     */

    /**
     * 获取最近更新
     * page:页码：从 1 开始
     * http://www.mangabz.com/manga-list-0-0-2-p4/ 第四页
     */
    @GET("manga-list-0-0-2-p{page}/")
    suspend fun getUpdateComic(@Path("page") page: Int): ResponseBody

    /**
     * 排行榜
     */
    @GET("manga-list{rank}-p{page}/")
    suspend fun getRankComic(@Path("rank") url: String, @Path("page") page: Int): ResponseBody

    /**
     * 分类内容
     */
    @GET("manga-list-{category}-p{page}/")
    suspend fun getCategoryComic(
        @Path("category") category: String,
        @Path("page") page: Int
    ): ResponseBody

    /**
     * 获取漫画内容
     */
    @GET("{id}/")
    suspend fun getComicDetail(@Path("id") comicId: String): ResponseBody


    /**
     * 获取章节内容
     * http://www.mangabz.com/m13454/chapterimage.ashx?cid=13454&page=1
     */
    @GET("m{chapterId}/chapterimage.ashx")
    suspend fun getComicContent(
        @Header("Referer") referer: String,
        @Path("chapterId") pid: String,
        @Query("cid") cid: String,
        @Query("page") page: Int
    ): ResponseBody

    /**
     * 搜索接口
     * http://www.mangabz.com/search?title=%E5%8F%B2%E8%8E%B1%E5%A7%86&page=2
     */
    @GET("search")
    suspend fun search(@Query("title") title: String, @Query("page") page: Int): ResponseBody
}