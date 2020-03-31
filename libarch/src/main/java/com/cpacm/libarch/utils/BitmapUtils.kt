package com.cpacm.libarch.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.Base64
import android.util.TypedValue

import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import android.graphics.drawable.Drawable
import com.cpacm.libarch.ArchApp
import com.cpacm.libarch.R


/**
 * 一些绘画常用方法
 *
 * @Auther: cpacm
 * @Date: 2016/1/6 0006-下午 12:18
 */
object BitmapUtils {

    //缩放系数
    val SCALE = 6

    fun createBlurredImageFromBitmap(bitmap: Bitmap, context: Context, inSampleSize: Int): Bitmap? {

        if (Build.VERSION.SDK_INT > 16) {
            val rs = RenderScript.create(context)
            val options = BitmapFactory.Options()
            options.inSampleSize = inSampleSize

            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val imageInByte = stream.toByteArray()
            val bis = ByteArrayInputStream(imageInByte)
            val blurTemplate = BitmapFactory.decodeStream(bis, null, options)

            val input = Allocation.createFromBitmap(rs, blurTemplate)
            val output = Allocation.createTyped(rs, input.type)
            val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
            script.setRadius(8f)
            script.setInput(input)
            script.forEach(output)
            output.copyTo(blurTemplate)
            return blurTemplate
        } else {
            return fastBlur(bitmap, 25f)
        }
        //return new BitmapDrawable(context.getResources(), blurTemplate);
    }

    /**
     * 快速模糊算法
     *
     * @param sbitmap
     * @param radiusf
     * @return Stack Blur v1.0 from
     * http://www.quasimondo.com/StackBlurForCanvas/StackBlurDemo.html
     * Java Author: Mario Klingemann <mario at quasimondo.com>
     * http://incubator.quasimondo.com
     * created Feburary 29, 2004
     * Android port : Yahel Bouaziz <yahel at kayenko.com>
     * http://www.kayenko.com
     * ported april 5th, 2012
     *
     *
     * This is a compromise between Gaussian Blur and Box blur
     * It creates much better looking blurs than Box Blur, but is
     * 7x faster than my Gaussian Blur implementation.
     * I called it Stack Blur because this describes best how this
     * filter works internally: it creates a kind of moving stack
     * of colors whilst scanning through the image. Thereby it
     * just has to add one new block of color to the right side
     * of the stack and remove the leftmost color. The remaining
     * colors on the topmost layer of the stack are either added on
     * or reduced by one, depending on if they are on the right or
     * on the left side of the stack.
     * If you are using this algorithm in your code please add
     * the following line:
     *
     *
     * Stack Blur Algorithm by Mario Klingemann <mario></mario>@quasimondo.com>
    </yahel></mario> */
    fun fastBlur(sbitmap: Bitmap, radiusf: Float): Bitmap? {
        val bitmap = Bitmap.createScaledBitmap(sbitmap, sbitmap.width / SCALE, sbitmap.height / SCALE, false)//先缩放图片，增加模糊速度
        val radius = radiusf.toInt()
        if (radius < 1) {
            return null
        }

        val w = bitmap.width
        val h = bitmap.height

        val pix = IntArray(w * h)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)

        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1

        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rsum: Int
        var gsum: Int
        var bsum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vmin = IntArray(Math.max(w, h))

        var divsum = div + 1 shr 1
        divsum *= divsum
        val dv = IntArray(256 * divsum)
        i = 0
        while (i < 256 * divsum) {
            dv[i] = i / divsum
            i++
        }

        yi = 0
        yw = yi

        val stack = Array(div) { IntArray(3) }
        var stackpointer: Int
        var stackstart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routsum: Int
        var goutsum: Int
        var boutsum: Int
        var rinsum: Int
        var ginsum: Int
        var binsum: Int

        y = 0
        while (y < h) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rsum += sir[0] * rbs
                gsum += sir[1] * rbs
                bsum += sir[2] * rbs
                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }
                i++
            }
            stackpointer = radius

            x = 0
            while (x < w) {

                r[yi] = dv[rsum]
                g[yi] = dv[gsum]
                b[yi] = dv[bsum]

                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum

                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]

                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vmin[x]]

                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff

                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]

                rsum += rinsum
                gsum += ginsum
                bsum += binsum

                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer % div]

                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]

                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]

                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            bsum = 0
            gsum = bsum
            rsum = gsum
            boutsum = rsum
            goutsum = boutsum
            routsum = goutsum
            binsum = routsum
            ginsum = binsum
            rinsum = ginsum
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x

                sir = stack[i + radius]

                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]

                rbs = r1 - Math.abs(i)

                rsum += r[yi] * rbs
                gsum += g[yi] * rbs
                bsum += b[yi] * rbs

                if (i > 0) {
                    rinsum += sir[0]
                    ginsum += sir[1]
                    binsum += sir[2]
                } else {
                    routsum += sir[0]
                    goutsum += sir[1]
                    boutsum += sir[2]
                }

                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackpointer = radius
            y = 0
            while (y < h) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = -0x1000000 and pix[yi] or (dv[rsum] shl 16) or (dv[gsum] shl 8) or dv[bsum]

                rsum -= routsum
                gsum -= goutsum
                bsum -= boutsum

                stackstart = stackpointer - radius + div
                sir = stack[stackstart % div]

                routsum -= sir[0]
                goutsum -= sir[1]
                boutsum -= sir[2]

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vmin[y]

                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]

                rinsum += sir[0]
                ginsum += sir[1]
                binsum += sir[2]

                rsum += rinsum
                gsum += ginsum
                bsum += binsum

                stackpointer = (stackpointer + 1) % div
                sir = stack[stackpointer]

                routsum += sir[0]
                goutsum += sir[1]
                boutsum += sir[2]

                rinsum -= sir[0]
                ginsum -= sir[1]
                binsum -= sir[2]

                yi += w
                y++
            }
            x++
        }
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

    fun drawableToBitmap(drawable: Drawable): Bitmap {

        if (drawable is BitmapDrawable) {
            val bd = drawable
            return bd.bitmap
        }
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        println("Drawable转Bitmap")
        val config = if (drawable.opacity != PixelFormat.OPAQUE)
            Bitmap.Config.ARGB_8888
        else
            Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(w, h, config)
        //注意，下面三行代码要用到，否则在View或者SurfaceView里的canvas.drawBitmap会看不到图
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        drawable.draw(canvas)

        return bitmap
    }

    fun getBitmapBytes(bitmap: Bitmap): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    fun getBitmapByString(imgSrc: String?): Bitmap? {
        if (imgSrc != null) {
            val buf = Base64.decode(imgSrc, Base64.NO_WRAP)
            val bas = ByteArrayInputStream(buf)
            return BitmapFactory.decodeStream(bas)
        } else
            return null
    }

    fun setBitmapByString(bitmap: Bitmap?): String? {
        if (bitmap != null) {
            val bos = ByteArrayOutputStream()
            //把图片位图按照JPEG格式压缩  压缩比率为50%,
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bos)
            //使用Base64编码将图片的byte数组转换成字符串
            return Base64.encodeToString(bos.toByteArray(), Base64.NO_WRAP)
        } else
            return null
    }

    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dp （DisplayMetrics类中属性density）
     * @return
     */
    fun dp2px(dp: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(),
                ArchApp.getInstance().getContext().resources.displayMetrics).toInt()
    }

    /**
     * 将px值转换为dip或dp值，保证尺寸大小不变
     *
     * @param pxValue （DisplayMetrics类中属性density）
     * @return
     */
    fun px2dp(pxValue: Float): Int {
        val scale = ArchApp.getInstance().getContext().resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun sp2px(spValue: Float): Int {
        val fontScale = ArchApp.getInstance().getContext().resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue （DisplayMetrics类中属性scaledDensity）
     * @return
     */
    fun px2sp(pxValue: Float): Int {
        val fontScale = ArchApp.getInstance().getContext().resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun getColorAccent(context: Context): Int {
        val typedValue = TypedValue()
        context.theme.resolveAttribute(R.attr.colorAccent, typedValue, true)
        return typedValue.data
    }

}
