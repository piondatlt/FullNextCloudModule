package example.datlt.nextcloud.database.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: Long,
    val name: String,
    val nameFolder: String,
    val path: String,
    val pathFolder: String,
    val typeMedia: String,
    val isSelect: Boolean,
    val numberWhenChoose: Int
) : Parcelable
