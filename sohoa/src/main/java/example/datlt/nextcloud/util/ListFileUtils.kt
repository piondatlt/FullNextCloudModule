package example.datlt.nextcloud.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import dagger.hilt.android.qualifiers.ApplicationContext
import example.datlt.nextcloud.database.entities.Photo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class ListFileUtils
@Inject
constructor(@ApplicationContext private val application: Context) {

    suspend fun getAllPhoto(onFinishGetVideo: (list: List<Photo>) -> Unit) = withContext(IO) {

        val listPhoto = mutableListOf<Photo>()


        val promise = async {
            launch {
                val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    arrayOf(
                        MediaStore.Files.FileColumns.MEDIA_TYPE,
                        MediaStore.Files.FileColumns.RELATIVE_PATH,
                        MediaStore.Files.FileColumns.DISPLAY_NAME,
                        MediaStore.Files.FileColumns._ID,
                        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                        MediaStore.Files.FileColumns.BUCKET_ID,
                    )
                else arrayOf(
                    MediaStore.Files.FileColumns.MEDIA_TYPE,
                    MediaStore.Files.FileColumns.DATA,
                    MediaStore.Files.FileColumns.TITLE,
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
                    MediaStore.Files.FileColumns.BUCKET_ID,
                )

                val mimeTypes = mutableListOf<String>()
                val extenstions = mutableListOf(
                    "jpg" , "JPG" , "png" , "PNG" , "jpeg" , "JPEG"
                )
                extenstions.forEach { mimeType ->
                    MimeTypeMap
                        .getSingleton()
                        .getMimeTypeFromExtension(mimeType)?.let {
                            mimeTypes.add("'$it'")
                        }
                }
                val uri = MediaStore.Files.getContentUri("external")
                application.contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
                )?.use { cursor ->
                    while (cursor.moveToNext()) {
                        try {
                            val idVideo: Long =
                                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                            val nameVideo: String =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    val name = cursor.getString(
                                        cursor.getColumnIndexOrThrow(
                                            MediaStore.Files.FileColumns.DISPLAY_NAME
                                        )
                                    )
                                    val index = name.lastIndexOf(".")
                                    if (index != -1) {
                                        val nameCut = name.substring(0, index)
                                        nameCut
                                    } else {
                                        name
                                    }
                                } else cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                        MediaStore.Files.FileColumns.TITLE
                                    )
                                )
                            val path =
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                    "sdcard/" + cursor.getString(
                                        cursor.getColumnIndexOrThrow(
                                            MediaStore.Files.FileColumns.RELATIVE_PATH
                                        )
                                    ) + cursor.getString(
                                        cursor.getColumnIndexOrThrow(
                                            MediaStore.Files.FileColumns.DISPLAY_NAME
                                        )
                                    )
                                else cursor.getString(
                                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                                )
                            val pathFolder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                                "sdcard/" + cursor.getString(
                                    cursor.getColumnIndexOrThrow(
                                        MediaStore.Files.FileColumns.RELATIVE_PATH
                                    )
                                )
                            else cursor.getString(
                                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
                            )
                            val folderName = File(pathFolder).name
                            val videoUri = Uri.withAppendedPath(uri, "" + idVideo)
                            val timeModified = File(path).lastModified()
                            val sp = path.split(".")
                            var typeMediaDetail = ""
                            if (!sp.isNullOrEmpty()) {
                                typeMediaDetail = sp[sp.size - 1].trim()
                                if (typeMediaDetail == "jpg"
                                    || typeMediaDetail == "JPG"
                                    || typeMediaDetail == "png"
                                    || typeMediaDetail == "PNG"
                                    || typeMediaDetail == "jpeg"
                                    || typeMediaDetail == "JPEG"
                                ) {
                                    //do nothing
                                } else {
                                    continue
                                }
                            }
                            listPhoto.add(
                                Photo(
                                    id = idVideo,
                                    name = nameVideo,
                                    typeMedia = typeMediaDetail,
                                    pathFolder = pathFolder,
                                    nameFolder = folderName,
                                    path = path,
                                    isSelect = false,
                                    numberWhenChoose = 0,
                                )
                            )
                        } catch (e: Exception) {

                        }
                    }
                }
            }
        }
        promise.await()
        withContext(Dispatchers.Main) {
            onFinishGetVideo.invoke(listPhoto)
        }
    }


//    suspend fun getAllVideo(onFinishGetVideo: (list: List<MediaObject>) -> Unit) = withContext(IO) {
//
//        val listVideo = mutableListOf<MediaObject>()
//        val listFolder = mutableListOf<String>()
//
//        val promise = async {
//            launch {
//                val projection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                    arrayOf(
//                        MediaStore.Files.FileColumns.MEDIA_TYPE,
//                        MediaStore.Files.FileColumns.RELATIVE_PATH,
//                        MediaStore.Files.FileColumns.DISPLAY_NAME,
//                        MediaStore.Files.FileColumns._ID,
//                        MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
//                        MediaStore.Files.FileColumns.BUCKET_ID,
//                    )
//                else arrayOf(
//                    MediaStore.Files.FileColumns.MEDIA_TYPE,
//                    MediaStore.Files.FileColumns.DATA,
//                    MediaStore.Files.FileColumns.TITLE,
//                    MediaStore.Files.FileColumns._ID,
//                    MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME,
//                    MediaStore.Files.FileColumns.BUCKET_ID,
//                )
//
//                val mimeTypes = mutableListOf<String>()
//                val extenstions = mutableListOf(
//                    "mp4" , "MP4" , "avi" , "AVI" , "MOV" , "mov" , "flv" , "FLV", "mpg" , "MPG" , "MKV" , "mkv"
//                )
//                extenstions.forEach { mimeType ->
//                    MimeTypeMap
//                        .getSingleton()
//                        .getMimeTypeFromExtension(mimeType)?.let {
//                            mimeTypes.add("'$it'")
//                        }
//                }
//                val uri = MediaStore.Files.getContentUri("external")
//                application.contentResolver.query(
//                    uri,
//                    projection,
//                    null,
//                    null,
//                    MediaStore.Files.FileColumns.DATE_MODIFIED + " DESC"
//                )?.use { cursor ->
//                    while (cursor.moveToNext()) {
//                        try {
//                            val idVideo: Long =
//                                cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
//                            val nameVideo: String =
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                                    val name = cursor.getString(
//                                        cursor.getColumnIndexOrThrow(
//                                            MediaStore.Files.FileColumns.DISPLAY_NAME
//                                        )
//                                    )
//                                    val index = name.lastIndexOf(".")
//                                    if (index != -1) {
//                                        val nameCut = name.substring(0, index)
//                                        nameCut
//                                    } else {
//                                        name
//                                    }
//                                } else cursor.getString(
//                                    cursor.getColumnIndexOrThrow(
//                                        MediaStore.Files.FileColumns.TITLE
//                                    )
//                                )
//                            val path =
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                                    "sdcard/" + cursor.getString(
//                                        cursor.getColumnIndexOrThrow(
//                                            MediaStore.Files.FileColumns.RELATIVE_PATH
//                                        )
//                                    ) + cursor.getString(
//                                        cursor.getColumnIndexOrThrow(
//                                            MediaStore.Files.FileColumns.DISPLAY_NAME
//                                        )
//                                    )
//                                else cursor.getString(
//                                    cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
//                                )
//                            val pathFolder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
//                                "sdcard/" + cursor.getString(
//                                    cursor.getColumnIndexOrThrow(
//                                        MediaStore.Files.FileColumns.RELATIVE_PATH
//                                    )
//                                )
//                            else cursor.getString(
//                                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA)
//                            )
//                            val folderName = File(pathFolder).name
//                            val videoUri = Uri.withAppendedPath(uri, "" + idVideo)
//                            val timeModified = File(path).lastModified()
//                            val sp = path.split(".")
//                            var typeMediaDetail = ""
//                            if (!sp.isNullOrEmpty()) {
//                                typeMediaDetail = sp[sp.size - 1].trim()
//                                if (typeMediaDetail == "mp4"
//                                    || typeMediaDetail == "MP4"
//                                    || typeMediaDetail == "avi"
//                                    || typeMediaDetail == "AVI"
//                                    || typeMediaDetail == "mov"
//                                    || typeMediaDetail == "MOV"
//                                    || typeMediaDetail == "flv"
//                                    || typeMediaDetail == "FLV"
//                                    || typeMediaDetail == "mpg"
//                                    || typeMediaDetail == "MVG"
//                                    || typeMediaDetail == "mkv"
//                                    || typeMediaDetail == "MKV"
//                                ) {
//                                    //do nothing
//                                } else {
//                                    continue
//                                }
//                            }
//                            listVideo.add(
//                                MediaObject(
//                                    id = idVideo,
//                                    name = nameVideo,
//                                    mediaType = typeMediaDetail,
//                                    folderPath = pathFolder,
//                                    folderName = folderName,
//                                    path = path
//                                )
//                            )
//                        } catch (e: Exception) {
//
//                        }
//                    }
//                }
//            }
//        }
//        promise.await()
//        withContext(Dispatchers.Main , {
//            onFinishGetVideo.invoke(listVideo)
//        })
//    }

}