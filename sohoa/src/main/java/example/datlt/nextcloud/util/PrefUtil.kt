package example.datlt.nextcloud.util

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefUtil
@Inject
constructor(
    private val sharedPreferences: SharedPreferences,
    private val editor: SharedPreferences.Editor
) {

    var token: String?
        get() = sharedPreferences.getString("CachedToken", null)
        set(value) {
            editor.putString("CachedToken", value).commit()
        }

    var permissionDeniedCount: Int
        get() = sharedPreferences.getInt("permissionDeniedCount", 0)
        set(value) {
            editor.putInt("permissionDeniedCount", value).commit()
        }

}