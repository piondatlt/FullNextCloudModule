package example.datlt.nextcloud.util

import android.os.Handler
import android.os.Looper
import android.util.Log
import java.lang.Exception

fun safeDelay(delayMillis: Long = 0, action: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        try {
            action()
        } catch (e: Exception) {
            Log.e("AppDebug", "safeDelay: $e")
        }
    }, delayMillis)
}