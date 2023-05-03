package example.datlt.nextcloud.util

import android.graphics.Rect
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import example.datlt.nextcloud.R

@BindingAdapter(value = ["seekbar"])
fun TextView.progressText(
    seekbar: SeekBar,
) {
    seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            this@progressText.text = progress.toString()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
            //do nothing
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
            //do nothing
        }

    })
}

@BindingAdapter("pathImage" , requireAll = true)
fun ImageView.setImageFromPath(pathImage : String?){
    if (pathImage != null){
        Glide.with(context).load(pathImage).into(this)
    }else{
        Glide.with(context).load(R.drawable.ic_error).into(this)
    }
}

@BindingAdapter(value =["actionDoubleClick" ] , requireAll = false)
fun View.setPreventClickScaleView(action : Runnable?){
    action ?: return

    setOnTouchListener(object : View.OnTouchListener {
        private var lastClickTime: Long = 0
        private var rect: Rect? = null

        override fun onTouch(v: View, event: MotionEvent): Boolean {
            fun setScale(scale: Float) {
                v.scaleX = scale
                v.scaleY = scale
            }

            if (event.action == MotionEvent.ACTION_DOWN) {
                //action down: scale view down
                rect = Rect(v.left, v.top, v.right, v.bottom)
                setScale(0.9f)
            } else if (rect != null && !rect!!.contains(
                    v.left + event.x.toInt(),
                    v.top + event.y.toInt()
                )
            ) {
                //action moved out
                setScale(1f)
                return false
            } else if (event.action == MotionEvent.ACTION_UP) {
                //action up
                setScale(1f)
                //handle click too fast
                if (SystemClock.elapsedRealtime() - lastClickTime < 500) {
                    //do nothing
                } else {
                    lastClickTime = SystemClock.elapsedRealtime()
                    action.run()
                }
            } else {
                //other
            }
            return true
        }
    })

}