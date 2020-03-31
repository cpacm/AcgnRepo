package com.cpacm.comic

import org.junit.Test
import java.util.regex.Pattern

/**
 *
 *
 *
 * @author cpacm 2020-01-13
 */
class RegMatchTest {

    //@Test
    fun match() {
        val js = "var img_qianzso = new Array();img_qianzso[3] = \"https://imgsh.dm365.top/h3/\";var webpshow = 0;"
        val list = js.split(";")
        val serverMap = hashMapOf<String, String>()
        for (server in list) {
            val reg1 = "\\[(.*?)\\]"
            val key = match(reg1, server, 1)
            val reg = "\"(.*?)\""
            val value = match(reg, server, 1)
            System.out.println(key + "=" + value)
//            if(server.contains("[") and server.contains("]")){
//                val start = server.indexOf("[")
//                var end = server.indexOf("]")
//                val key = server.substring(start,end)
//                val
//            }
        }

    }

    @Test
    fun match2() {
        val js = "\$(document).ready(function() {\n" +
                "\t\t\t\t\$('#page-selection').bootpag({\n" +
                "\t\t\ttotal: 32,\n" +
                "\t\t\tpage: 1,\n" +
                "\t\t\tmaxVisible: 10,\n" +
                "\t\t\thref: \"/list/{{number}}.html\",\n" +
                "\t\t\tleaps: false, \n" +
                "\t\t\tfirstLastUse: true,\n" +
                "\t\t\tfirst: '←',\n" +
                "\t\t\tlast: '→'\n" +
                "\t\t});\n" +
                "\t\t\t});"
        val value = match("total:(.*?),",js,1)
        System.out.println(value)
    }

    fun match(regex: String?, input: String?, group: Int): String? {
        try {
            val pattern = Pattern.compile(regex)
            val matcher = pattern.matcher(input)
            if (matcher.find()) {
                for(i in 0 until matcher.groupCount()){
                    System.out.println(matcher.group(i))
                }
                return matcher.group(group).trim { it <= ' ' }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}