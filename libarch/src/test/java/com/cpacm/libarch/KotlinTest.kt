package com.cpacm.libarch

import org.junit.Test

/**
 *
 * <p>
 *
 * @author cpacm 2018/1/8
 */
class KotlinTest {

    @Test
    fun KotlinNull() {
        val a = null
        val b = 3 + 4
        print(a ?: "empty")
    }

    @Test
    fun kotlinRegex() {
        val p = """(?<=(cv|/))\d+""".toRegex()
        val result = p.find("https://www.bilibili.com/read/cv/1144782?from=category_2")
        print(result?.value)
    }

    @Test
    fun kotlinRegex2() {
        val p = """(?<=(p/))[0-9a-zA-Z]+""".toRegex()
        val result = p.find("https://www.jianshu.com/p/cad89903ed1f")
        print(result?.value)
    }
    @Test
    fun kotlinRegex3() {
        val p = "(?<=(initData\\(\\{))(.+?)(?=\\})".toRegex()
        val result = p.find("""${'$'}(document).ready(function(){
            mReader.initData({"id":68141,"comic_id":28220,"chapter_name":"\u8054\u52a8\u5ba3\u4f20 \u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\uff01\u00d7\u6a31\u53f6\u5b66\u59d0\u662f\u521d\u604b\u00d7\u5728\u9b54\u738b\u57ce\u8bf4\u665a\u5b89\u00d7\u821e\u4f0e\u5bb6\u7684\u6599\u7406\u4eba\u00d7\u96cf\u7684\u6c14\u6c1b\u653b\u7565\u00d7\u53e4\u89c1\u540c\u5b66\u6709\u4ea4\u6d41\u969c\u788d\u75c7\u00d7\u521d\u604b\u50f5\u5c38","chapter_order":90,"createtime":1502344452,"folder":"t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613","page_url":["https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/001.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/002.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/003.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/004.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/005.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/006.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/007.jpg","https:\/\/images.dmzj.com\/t\/\u5929\u91ce\u60e0\u6d51\u8eab\u662f\u7834\u7efd\/\u8054\u52a8\u5ba3\u4f20_1502342613\/008.jpg"],"chapter_type":0,"chaptertype":17,"chapter_true_type":3,"chapter_num":0,"updatetime":1502344452,"sum_pages":8,"sns_tag":1,"uid":103609416,"username":"\u771f\u6b63de\u7c89\u4e1d","translatorid":"","translator":"","link":"","message":"","download":"","hidden":0,"direction":0,"filesize":651145,"high_file_size":0,"picnum":8,"hit":6,"next_chap_id":74574,"prev_chap_id":67850,"comment_count":212}, "天野惠浑身是破绽！", "webpic/19/tianyehuihunshenshipozhan0626ll.jpg");
			${'$'}("head").append('<meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=1.0,maximum-scale=3.0 user-scalable=yes"/>')
        });	""")
        print(result?.value)
    }
}