package example.datlt.nextcloud.util

import android.content.Context
import android.util.TypedValue
import androidx.fragment.app.Fragment

object DeviceDimensionsHelper {
    // DeviceDimensionsHelper.getDisplayWidth(context) => (display width in pixels)
    fun Context.getDisplayWidth(): Float {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels.toFloat()
    }

    // DeviceDimensionsHelper.getDisplayHeight(context) => (display height in pixels)
    fun Context.getDisplayHeight(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels
    }

    // DeviceDimensionsHelper.convertDpToPixel(25f, context) => (25dp converted to pixels)
    fun Context.convertDpToPixel(dp: Float): Float {
        val r = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }

    fun Fragment.convertDpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }

    // DeviceDimensionsHelper.convertPixelsToDp(25f, context) => (25px converted to dp)
    fun Context.convertPixelsToDp(px: Float): Float {
        val r = resources
        val metrics = r.displayMetrics
        return px / (metrics.densityDpi / 160f)
    }

    // DeviceDimensionsHelper.convertSpToPixel(25f, context) => (25sp converted to pixels)
    fun Context.convertSpToPixel(sp: Float): Float {
        val r = resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.displayMetrics)
    }

    fun Fragment.convertSpToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        )
    }

    // Not tested yet
    fun Context.convertPixelsToSp(px: Float): Float {
        return px / resources.displayMetrics.scaledDensity
    }
}
