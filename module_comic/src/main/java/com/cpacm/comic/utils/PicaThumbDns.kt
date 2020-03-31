package com.cpacm.comic.utils

import com.cpacm.comic.model.ComicSettingManager
import okhttp3.Dns
import java.net.InetAddress

/**
 * <p>
 *     pica地址
 * @author cpacm 2020-02-02
 */
class PicaThumbDns : Dns {
    override fun lookup(hostname: String): List<InetAddress> {
        val address = ComicSettingManager.getInstance().getSetting(ComicSettingManager.KEYWORD_COMIC_PICA_ADDRESS_SET, mutableSetOf())
        try {
            val arrayList = arrayListOf<InetAddress>()
            if (address.isEmpty()) {
                return Dns.SYSTEM.lookup(hostname)
            }
            arrayList.addAll(Dns.SYSTEM.lookup(hostname))
            for (add in address) {
                arrayList.addAll(InetAddress.getAllByName(add))
            }
            return arrayList
        } catch (e: Exception) {
            e.printStackTrace()
            return Dns.SYSTEM.lookup(hostname)
        }
    }
}