package example.datlt.nextcloud.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import example.datlt.nextcloud.framework.presentation.crop.StateData
import java.io.File

fun Context.cropBitmapFromPath(
    path: String,
    state: StateData,
    onCropDone: (bitmap: Bitmap?) -> Unit
) {
    val file = File(path)
    if (!file.exists()) {
        onCropDone.invoke(null)
    }
    var sourceBitmap = BitmapFactory.decodeFile(file.absolutePath)
    val rect = state.rectCrop
    Log.d("CHECKPATHCROPPED", "cropBitmapFromPath: ${rect.left} ${rect.top} ${rect.right} ${rect.bottom}")

    var left = rect.left
    var top = rect.top
    var right = rect.width()
    var bottom = rect.height()

    if (left < 0) left = 0
    if (top < 0) top = 0
    if (left + right > sourceBitmap.width) right = sourceBitmap.width - left
    if (top + bottom > sourceBitmap.height) bottom = sourceBitmap.height - top

    var cropBitmap =
        Bitmap.createBitmap(sourceBitmap, left, top, right, bottom)
    sourceBitmap = null

    //rotate
    if (state.rotateDegree != 0) {
        val matrix = Matrix()
        matrix.postRotate(state.rotateDegree.toFloat())
        val rotatedBitmap =
            Bitmap.createBitmap(cropBitmap, 0, 0, cropBitmap.width, cropBitmap.height, matrix, true)
        onCropDone.invoke(rotatedBitmap)
        cropBitmap = null
    } else {
        onCropDone.invoke(cropBitmap)
    }
}