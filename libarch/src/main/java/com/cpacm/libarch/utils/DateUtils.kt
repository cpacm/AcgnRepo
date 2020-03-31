package com.cpacm.libarch.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间日期工具
 *
 * @Auther: cpacm
 * @Date: 2015/12/29 0029-下午 2:48
 */
object DateUtils {

    /**
     * 返回unix时间戳 (1970年至今的秒数)
     *
     * @return
     */
    val unixStamp: Long
        get() = System.currentTimeMillis() / 1000

    /**
     * 得到昨天的日期
     *
     * @return
     */
    val yesterdayDate: String
        get() {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DATE, -1)
            val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            return sdf.format(calendar.time)
        }

    /**
     * 得到今天的日期 yyyy-mm-dd
     *
     * @return
     */
    val todayDate: String
        get() {
            val sdf = SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault())
            return sdf.format(Date())
        }

    /**
     * 得到今天的日期,mm-dd
     *
     * @return
     */
    val todayDateMD: String
        get() {
            val sdf = SimpleDateFormat("MM月dd日", Locale.getDefault())
            return sdf.format(Date())
        }

    /**
     * 获取追番时间表，为今天的前后6天，即总共13天
     *
     * @return
     */
    val bangumiDateList: List<Date>
        get() {
            val dateList = ArrayList<Date>()
            val calendar = GregorianCalendar()
            calendar.time = Date()
            calendar.add(Calendar.DATE, -6)
            for (i in 0..12) {
                dateList.add(calendar.time)
                calendar.add(Calendar.DATE, 1)
            }
            return dateList
        }


    /**
     * 获取今天0时的时间戳
     *
     * @return
     */
    val todayTime: Long
        get() {
            val cal = Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, 0)
            cal.set(Calendar.SECOND, 0)
            cal.set(Calendar.MINUTE, 0)
            cal.set(Calendar.MILLISECOND, 0)
            return cal.timeInMillis
        }

    fun zeroTime(time: Long): Long {
        val cal = Calendar.getInstance()
        cal.time = Date(time)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.timeInMillis
    }

    /**
     * 获取昨天0时的时间戳
     *
     * @return
     */
    val yesterdayTime: Long
        get() = todayTime - 24 * 3600 * 1000

    /**
     * 获取最近10年的年份
     *
     * @return
     */
    val recentYears: Array<String?>
        get() {
            val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
            val currentYear = sdf.format(System.currentTimeMillis())
            val year = Integer.parseInt(currentYear)
            val arr = arrayOfNulls<String>(11)
            arr[10] = "其他"
            for (i in 0..9) {
                arr[i] = (year - i).toString()
            }
            return arr
        }

    val curYear: String
        get() {
            val sdf = SimpleDateFormat("yyyy", Locale.getDefault())
            return sdf.format(System.currentTimeMillis())
        }


    // 获取某个日期的开始时间
    fun getDayStartTime(d: Date?): Long {
        val calendar = Calendar.getInstance()
        if (null != d)
            calendar.time = d
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    // 获取某个日期的结束时间
    fun getDayEndTime(d: Date?): Long {
        val calendar = Calendar.getInstance()
        if (null != d)
            calendar.time = d
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        return calendar.timeInMillis
    }

    /**
     * 返回星期几
     * */
    fun getWeekday(time: Long): String {
        val date = Date(time)
        val weekOfDays = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar = Calendar.getInstance()
        calendar.time = date
        var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
        w = if (w < 0) 0 else w
        return weekOfDays[w]
    }

    /**
     * 返回星期几
     * */
    fun getWeek(time: Long): Int {
        val date = Date(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        var w = calendar.get(Calendar.DAY_OF_WEEK) - 1
        w = if (w < 0) 0 else w
        return w
    }

    fun getMonth(time: Long): Int {
        val date = Date(time)
        val calendar = Calendar.getInstance()
        calendar.time = date
        val w = calendar.get(Calendar.MONTH) + 1
        return w
    }

    fun getBangumiMonth(): String {
        val m = getMonth(System.currentTimeMillis())
        when (m) {
            1, 2, 3 -> return "一月新番"
            4, 5, 6 -> return "四月新番"
            7, 8, 9 -> return "七月新番"
            10, 11, 12 -> return "十月新番"
        }
        return "新番表"
    }

    /**
     * 时间戳转化为对应时间格式
     * <yyyy年> <MM月dd日><yyyy-MM-dd HH:mm>
     * @return
     */
    fun formatTime(time: Long, pattern: String): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(time)
    }

    /**
     * 得到日期   MM-dd
     *
     * @param timeStamp 时间戳
     * @return
     */
    fun formatDate(timeStamp: Long): String {
        return formatTime(timeStamp, "yyyy年MM月dd日")
    }

    fun timelineFormat(mills: Long): String {
        val time = todayTime
        if (mills > time) {
            return "今日"
        }
        if (mills > yesterdayTime) {
            return "昨日"
        }
        return formatTime(mills, "MM月dd日")
    }

    fun historyTime(mills: Long): String {
        if (mills > todayTime) {
            return formatTime(mills, "HH:mm")
        }
        if (mills > yesterdayTime) {
            return "昨日" + formatTime(mills, "HH:mm")
        }
        return formatTime(mills, "MM月dd日 HH:mm")
    }

    /**
     * 得到时间  HH:mm
     */
    fun getCurrentTime(): String? {
        var time: String? = null
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = sdf.format(System.currentTimeMillis())
        val split = date.split("\\s".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (split.size > 1) {
            time = split[1]
        }
        return time
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @param timeStamp
     * @return
     */
    fun convertTimeToFormat(timeStamp: Long): String {
        val curTime = System.currentTimeMillis()
        val time = curTime - timeStamp

        return if (time >= 0 && time < 60) {
            "刚刚"
        } else if (time >= 60 && time < 3600) {
            (time / 60).toString() + "分钟前"
        } else if (time >= 3600 && time < 3600 * 24) {
            (time / 3600).toString() + "小时前"
        } else {
            formatTime(timeStamp, "yyyy年MM月dd日")
        }
    }

    /**
     * 此函数非原创，从网上搜索而来，timeZoneOffset原为int类型，为班加罗尔调整成float类型
     * timeZoneOffset表示时区，如中国一般使用东八区，因此timeZoneOffset就是8
     * @param timeZoneOffset -12 - 13
     * @return
     */
    fun getFormatedDateString(tZOffset: Float): String {
        var timeZoneOffset = tZOffset
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

        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = timeZone
        return sdf.format(Date())
    }
}
