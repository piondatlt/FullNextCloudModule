package example.datlt.nextcloud

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.zxy.recovery.core.Recovery
import dagger.hilt.android.HiltAndroidApp
import example.datlt.nextcloud.framework.MainActivity
import timber.log.Timber

@HiltAndroidApp
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        //TODO: enable/disable dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        if (BuildConfig.DEBUG) {
            Recovery.getInstance()
                .debug(true)
                .recoverInBackground(false)
                .recoverStack(true)
                .mainPage(MainActivity::class.java)
                .recoverEnabled(true)
                .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                .init(this)

            Timber.plant(Timber.DebugTree())
        }
    }

}
