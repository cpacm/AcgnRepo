package com.cpacm.comic

import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnsuanTest {


    @Test
    fun unsuanTest(){
        val value = "poiuytrewqxioqexyf"
        System.out.println(unsuan(value))
    }

    private fun unsuan(strFile: String): String {
        var str = strFile
        val num = str.length - str[str.length - 1].toInt() + 'a'.toInt()
        var code = str.substring(num - 13, num - 2)
        val cut = code.substring(code.length - 1)
        str = str.substring(0, num - 13)
        code = code.substring(0, code.length - 1)
        for (i in 0 until code.length) {
            str = str.replace(code[i], ('0'.toInt() + i).toChar())
        }
        val builder = StringBuilder()
        val array = str.split(cut)
        for (i in array.indices) {
            builder.append(Integer.parseInt(array[i]).toChar())
        }
        return builder.toString()
    }
}
