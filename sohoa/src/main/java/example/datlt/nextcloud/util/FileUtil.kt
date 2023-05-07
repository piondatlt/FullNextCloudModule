package example.datlt.nextcloud.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.util.Log
import example.datlt.nextcloud.util.Constant.FOLDER_NAME
import example.datlt.nextcloud.util.Constant.TEMP_CROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun Context.removeTempFile(tempFileName: String) {
    val pathFolder = "${filesDir.path}/$tempFileName"
    val folder = File(pathFolder)
    if (File(pathFolder).exists()) {
        val files = folder.listFiles()?.sortedWith(compareBy { it.name })
        if (files != null && files.isNotEmpty()) {
            for (item in files) {
                item.delete()
            }
        }
    }
}

fun Context.removeTempDocument() {
    val pathFolder =
        "${Environment.getExternalStorageDirectory().absoluteFile}/${Environment.DIRECTORY_DOCUMENTS}/$FOLDER_NAME"

    if (!File(pathFolder).exists()) {
        return
    }

    CoroutineScope(Dispatchers.IO).launch {
        val listFile = File(pathFolder).listFiles()
        if (!listFile.isNullOrEmpty()) {
            for (file in listFile) {
                file.delete()
            }
        }
    }
}

fun Context.saveBitmapToTempFile(bitmap: Bitmap, nameImage: String, tempFileName: String): String {
    val pathFolder = "${filesDir.path}/$tempFileName"

    if (!File(pathFolder).exists()) {
        File(pathFolder).mkdirs()
    }

    val filePath = "$pathFolder/$nameImage"
    val file = File(filePath)
    if (file.createNewFile()) {
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } catch (e: Exception) {
            Log.d("CHECKSAVED", "saveBitmap: $e")
            return ""
        }
        return file.path
    }
    return ""
}

fun Context.getAllCropBitmap(): List<String> {
    val listFile = mutableListOf<String>()
    val tempFile = TEMP_CROP
    val pathFolder = "${filesDir.path}/$tempFile"
    val folder = File(pathFolder)
    if (folder.exists()) {
        val files = folder.listFiles()?.sortedWith(compareBy { it.name })
        if (files != null && files.isNotEmpty()) {
            for (item in files) {
                listFile.add(item.path)
            }
        }
    }
    return listFile
}

fun Context.getAllFileInFolder(nameFolder: String): List<String> {
    val listFile = mutableListOf<String>()
    val pathFolder = "${filesDir.path}/$nameFolder"
    val folder = File(pathFolder)
    if (folder.exists()) {
        val files = folder.listFiles()?.sortedWith(compareBy { it.name })
        if (files != null && files.isNotEmpty()) {
            for (item in files) {
                listFile.add(item.path)
            }
        }
    }
    return listFile
}

fun Context.getAllCreatedFile(): List<String> {
    val listFile = mutableListOf<String>()
    val pathFolder =
        "${Environment.getExternalStorageDirectory().absoluteFile}/${Environment.DIRECTORY_DOCUMENTS}/$FOLDER_NAME"
    if (!File(pathFolder).exists()) {
        return listFile
    }

    val folder = File(pathFolder)
    if (folder.exists()) {
        val files = folder.listFiles()?.sortedWith(compareByDescending { it.lastModified() })
        if (files != null && files.isNotEmpty()) {
            for (item in files) {
                listFile.add(item.path)
            }
        }
    }
    return listFile
}

fun Context.createDocumentFile(nameFile: String): String {
    val pathFolder =
        "${Environment.getExternalStorageDirectory().absoluteFile}/${Environment.DIRECTORY_DOCUMENTS}/$FOLDER_NAME"

    if (!File(pathFolder).exists()) {
        File(pathFolder).mkdirs()
    }

    var filePath = "$pathFolder/$nameFile.pdf"
    var file = File(filePath)
    var tempValue = 1
    while (file.exists()) {
        filePath = "$pathFolder/$nameFile ($tempValue).pdf"
        file = File(filePath)
        tempValue++
    }

    if (file.createNewFile()) {
        return filePath
    }
    return ""
}