package com.cpacm.comic.model.bean

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * <p>
 *     漫画站点
 * @author cpacm 2020-01-11
 */
data class ComicSiteBean(
    @DrawableRes val logo: Int,
    @DrawableRes val cover: Int,
    @StringRes val title: Int,
    val site: String
)