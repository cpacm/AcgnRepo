package com.cpacm.comic.utils;

/**
 * <p>
 *     解析压缩字符
 *     eval(function(p,a,c,k,e,d){e=function(c){return(c<a?'':e(parseInt(c/a)))+((c=c%a)>35?String.fromCharCode(c+29):c.toString(36))};if(!''.replace(/^/,String)){while(c--){d[e(c)]=k[c]||e(c)}k=[function(e){return d[e]}];e=function(){return'\\\\w+'};c=1};while(c--){if(k[c]){p=p.replace(new RegExp('\\\\b'+e(c)+'\\\\b','g'),k[c])}}return p}('3 O=\"r\";3 q=\\'0/2/p.1|0/2/o.1|0/2/n.1|0/2/m.1|0/2/l.1|0/2/h.1|0/2/j.1|0/2/i.1|0/2/e.1|0/2/9.1|0/2/a.1|0/2/b.1|0/2/d.1|0/2/g.1|0/2/f.1|0/2/s.1|0/2/k.1|0/2/u.1|0/2/F.1|0/2/N.1|0/2/M.1|0/2/L.1|0/2/t.1|0/2/K.1\\';3 J=I;3 H=G;3 E=\\'8://7.6.5/0/v.4\\';3 D=\\'第C话\\';3 B=\\'\\';3 A=\\'\\';3 z=\\'第y话\\';3 x=\\'8://7.6.5/w.4\\';3 c=\\'异世界的兽医事业\\';',51,51,'202001|jpg|09|var|html|net|177mh|www|https|0143509439|01435017010|01435036911|linkn_z|01435076012|0143502588|01435072514|01435062413|0143504505|0143509427|0143502556|01435081116|0143504914|0143504923|0143504272|0143502171|0143502880|msg|gn|01435081415|01435028822|01435079917|437393|colist_244250|link_z|27|linkname|nextName_b|nextLink_b|26|preName_b|preLink_b|01435033818|51|img_s|24|maxPage|01435094923|01435039321|01435021220|01435089619|atsvr'.split('|'),0,{}))
 *
 * @author cpacm 2020-01-13
 */
public class JsPackerDecoder {

    public static String getDecodeJS(String data) {
        int beginIndex, endIndex;

        System.out.println("DATA: " + data);

        beginIndex = data.indexOf("}(");
        beginIndex = data.indexOf("'", beginIndex) + 1;
        endIndex = data.indexOf(";'", beginIndex);
        endIndex +=1;
        String encodePic = data.substring(beginIndex, endIndex);
        System.out.println("ENCODE: " + encodePic);

        endIndex = data.indexOf("split", beginIndex);
        endIndex = data.lastIndexOf("'", endIndex);
        beginIndex = data.lastIndexOf("'", endIndex - 1) + 1;
        String[] codes = data.substring(beginIndex, endIndex).split("\\|");
        for (int i = 0; i < codes.length; i++) {
            //Common.debugPrintln( "" + i+ " " + codes[i] );
        }
        return getDecode(codes, encodePic);
    }

    // ex. 0/i.1 ... -> 201306/220131103le3senmlyq.jpg ...
    public static String getDecode(String[] codes, String encode) {
        String decode = "";
        String temp = "";

        //for ( int i = 0; i < encode.length(); i ++ )

        int i = 0;
        while (i < encode.length()) {
            String a = encode.substring(i, i + 1);
            String b = "";
            if (i + 2 < encode.length())
                b = encode.substring(i + 1, i + 2);
            else
                b = "++++++++++++";
            boolean decodeA = false;
            boolean decodeB = false;

            i++;

            if ((a.charAt(0) >= '0' && a.charAt(0) <= '9') ||
                    (a.charAt(0) >= 'a' && a.charAt(0) <= 'z') ||
                    (a.charAt(0) >= 'A' && a.charAt(0) <= 'Z')) {
                decodeA = true;
            }
            if ((b.charAt(0) >= '0' && b.charAt(0) <= '9') ||
                    (b.charAt(0) >= 'a' && b.charAt(0) <= 'z') ||
                    (b.charAt(0) >= 'A' && b.charAt(0) <= 'Z')) {
                decodeB = true;
            }

            if (decodeA && decodeB) {
                temp = codes[getIndexOfE(a + b, codes.length)];

                if ("".equals(temp)) {
                    decode += (a + b);
                } else {
                    decode += temp;
                }
                i++;
            } else if (decodeA) {
                temp = codes[getIndexOfE(a, codes.length)];

                if ("".equals(temp)) {
                    decode += a;
                } else {
                    decode += temp;
                }
            } else {
                decode += a;
            }
        }

        return decode;
    }

    // ex. a -> 10 .
    public static int getIndexOfE(String eStr, int radix) {
        for (int i = 0; i < radix; i++) {
            String s = e(i, radix);
            //Common.debugPrintln( "-> " + s )
            if (eStr.equals(s)) {
                return i;
            } else {
                //Common.debugPrintln( " " + eStr + " : " + e( i ) );
            }
        }
        //Common.debugPrintln( "not found: " + eStr );
        return -1;
    }

    // ex. 10 -> a .
    public static String e(int c, int radix) {
        int a = 62; //radix;
        return (c < a ? "" : e((c / a), a)) + ((c = c % a) > 35 ? fromCharCode(c + 29) : Long.toString((long) c, 36));
    }

    // char's ascii code -> String .
    public static String fromCharCode(int... codePoints) {
        return new String(codePoints, 0, codePoints.length);
    }
}
