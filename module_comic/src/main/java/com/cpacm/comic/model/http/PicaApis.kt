package com.cpacm.comic.model.http

import com.cpacm.comic.ComicApp
import com.cpacm.comic.model.ComicSettingManager
import com.cpacm.comic.model.bean.*
import com.cpacm.libarch.model.ApiResponse
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * <p>
 *     pica api
 * @author cpacm 2019-12-03
 */
interface PicaApis {

    companion object {
        var BASE_URL = "https://picaapi.picacomic.com/"
        const val PICA_BASE_HOST = "picaapi.picacomic.com"
        const val BASE_MEDIA = "https://s3.picacomic.com/static/"
    }

    @GET("init?platform=android")
    suspend fun init(): ApiResponse<PicaInitialBean>


    @POST("auth/sign-in")
    suspend fun login(@Body info: RequestBody): ApiResponse<PicaTokenBean>


    @GET("init?platform=android")
    suspend fun initConfig(): ApiResponse<PicaInitialBean>

    @GET("categories")//categories
    suspend fun getCategory(): ApiResponse<PicaCategoryListBean>

    /**
     * 最近更新
     */
    @GET("comics?s=dd") //comics?page=1&s=dd
    suspend fun recentlyUpdate(@Query("page") page: Int): ApiResponse<PicaComicListBean>

    /**
     * 分类列表
     */
    @GET("comics?s=dd") //comics?page=1&c=短篇&s=dd
    suspend fun getCategoryList(
        @Query("page") page: Int,
        @Query("c") category: String
    ): ApiResponse<PicaComicListBean>

    /**
     * 作者列表
     */
    @GET("comics") //comics?page=1&a=水龙敬
    suspend fun getAuthorList(
        @Query("page") page: Int,
        @Query("a") category: String
    ): ApiResponse<PicaComicListBean>

    /**
     * 标签列表
     */
    @GET("comics") //comics?page=1&t=妹妹
    suspend fun getTagList(
        @Query("page") page: Int,
        @Query("t") category: String
    ): ApiResponse<PicaComicListBean>

    /**
     * 汉化组列表，普通搜索也可以实现
     */
    @GET("comics") //comics?page=1&ct=脸肿
    suspend fun getTransList(
        @Query("page") page: Int,
        @Query("t") category: String
    ): ApiResponse<PicaComicListBean>

    /**
     * 搜索
     * {
    "keyword": "幼",
    "sort": "dd"
    }
     */
    @POST("comics/advanced-search") //comics/advanced-search?page=1
    suspend fun search(
        @Body search: SortBean,
        @Query("page") page: Int
    ): ApiResponse<PicaComicListBean>

    /**
     * 排行榜
     * tt= H24-24小时内 D7-7天内 D30-30天
     */
    @GET("comics/leaderboard?ct=VC")//comics/leaderboard?tt=H24&ct=VC 无分页，只有40个
    suspend fun rankComic(@Query("tt") page: String): ApiResponse<PicaComicRankBean>

    /**
     * 漫画详情
     */
    @GET("comics/{id}")//comics/5dceb2e217be2902eca7306a
    suspend fun getComicDetail(@Path("id") comicId: String): ApiResponse<PicaComicDetail>

    /**
     * 漫画分卷
     */
    @GET("comics/{id}/eps")//comics/5dceb2e217be2902eca7306a/eps?page=1
    suspend fun getComicEp(
        @Path("id") comicId: String,
        @Query("page") page: Int
    ): ApiResponse<PicaEpListBean>

    /**
     * 漫画内容
     */
    @GET("comics/{comicId}/order/{order}/pages")//comics/5dceb2e217be2902eca7306a/order/1/pages?page=1
    suspend fun getComicContent(
        @Path("comicId") comicId: String,
        @Path("order") order: String,
        @Query("page") page: Int
    ): ApiResponse<PicaContentListBean>

    interface PicaAddressApis {
        @GET("http://68.183.234.72/init")
        suspend fun getApiAddress(): PicaAddressBean

        @GET("http://206.189.95.169/init")
        suspend fun getApiAddress2(): PicaAddressBean
    }

}