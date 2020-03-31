package com.cpacm.libarch

import org.junit.Test
import java.text.SimpleDateFormat
import java.util.*

/**
 * <p>
 *
 * @author cpacm 2019-05-06
 */
class DateTest {

    /**
     * 此函数非原创，从网上搜索而来，timeZoneOffset原为int类型，为班加罗尔调整成float类型
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     * @param timeZoneOffset -12 - 13
     * @return
     */
    @Test
    fun getFormatedDateString() {
        println(System.currentTimeMillis())
        var timeZoneOffset = 0f
        if (timeZoneOffset > 13 || timeZoneOffset < -12) {
            timeZoneOffset = 0f
        }

        val newTime = (timeZoneOffset * 60f * 60f * 1000f).toInt()
        val timeZone: TimeZone
        val ids = TimeZone.getAvailableIDs(newTime)
        if (ids.size == 0) {
            timeZone = TimeZone.getDefault()
        } else {
            timeZone = SimpleTimeZone(newTime, ids[0])
        }

        val sdf = SimpleDateFormat("yyyy-MM-dd HH")
        val sdf2 = SimpleDateFormat("yyyy-MM-dd HH")
        sdf.timeZone = timeZone
        sdf.calendar.timeInMillis
        val date = sdf.format(Date())
        println(date)
        println(sdf2.parse(date).time)
        println(System.currentTimeMillis())
    }
}