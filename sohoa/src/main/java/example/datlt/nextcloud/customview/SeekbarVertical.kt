package example.datlt.nextcloud.customview

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import example.datlt.nextcloud.util.convertDpToPx
import pion.tech.magnifier2.framework.customview.viewinterface.CustomSeekbarInterface

class SeekbarVertical(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val backGroundRect = RectF()
    private val backGroundPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val roundRadius = context.convertDpToPx(30)

    private var thumbRadius = 0f
    private var thumb: Bitmap? = null
    private val thumbRect = RectF()
    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG)


    private val minProgress = 0
    private val maxProgress = 100
    private var progress = 0


    private var backGroundHeight = 0f

    private var listener: CustomSeekbarInterface? = null


    init {
        backGroundPaint.color = Color.BLACK
        backGroundPaint.alpha = 127
        val sourceThumb =
            attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "src", 0)
        thumb = ContextCompat.getDrawable(context, sourceThumb)
            ?.let { drawableToBitmap(it) }
    }


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        thumbRadius = width.toFloat() / 2
        val backgroundWidth = width.toFloat() / 22 * 6
        val backGroundLeft = (width.toFloat() / 2) - (backgroundWidth / 2)
        val backgroundRight = (width.toFloat() / 2) + (backgroundWidth / 2)
        backGroundHeight = height - (thumbRadius * 2)
        backGroundRect.set(backGroundLeft, thumbRadius, backgroundRight, height - thumbRadius)

        syncThumbRectToProgress()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBackground(canvas)
        drawThumb(canvas)
    }

    private fun drawBackground(canvas: Canvas?) {
        canvas ?: return
        canvas.drawRoundRect(
            backGroundRect, roundRadius.toFloat(), roundRadius.toFloat(), backGroundPaint
        )
    }

    fun getProgress(): Int = progress
    fun setProgress(value: Int) {
        progress = value
        syncThumbRectToProgress()
        postInvalidate()
    }

    fun setProgressChangeListener(listener: CustomSeekbarInterface) {
        this@SeekbarVertical.listener = listener
    }

    private fun drawThumb(canvas: Canvas?) {
        canvas ?: return
        thumb?.let { canvas.drawBitmap(it, null, thumbRect, thumbPaint) }
    }

    private fun syncThumbRectToProgress() {
        val top = (backGroundHeight) * (100 - progress) / 100
        val bottom = top + (thumbRadius * 2)
        thumbRect.set(0f, top, width.toFloat(), bottom)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            val bitmapDrawable: BitmapDrawable = drawable as BitmapDrawable
            if (bitmapDrawable.bitmap != null) {
                return bitmapDrawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            ) // Single color bitmap will be created of 1x1 pixel
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                //do nothing
                listener?.onStartToTouch()
            }
            MotionEvent.ACTION_MOVE -> {
                if (event.y >= height - thumbRadius) {
                    progress = 0
                    return true
                } else if (event.y <= thumbRadius) {
                    progress = 100
                    return true
                } else {
                    progress = ((1 - ((event.y - thumbRadius) / backGroundHeight)) * 100).toInt()
                }
                if (progress < 0) {
                    progress = 0
                }
                if (progress > 100) {
                    progress = 100
                }
                syncThumbRectToProgress()
                postInvalidate()
                listener?.onProgressChange(progress)
            }
            MotionEvent.ACTION_UP -> {
                //do nothing
                listener?.onStopToTouch()
            }
        }
        return true
    }
}