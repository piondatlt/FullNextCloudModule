package example.datlt.nextcloud.framework.presentation.camera

import android.graphics.Bitmap
import android.graphics.PointF
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.PictureResult
import example.datlt.nextcloud.R
import example.datlt.nextcloud.util.Constant.TEMP_CAMERA
import example.datlt.nextcloud.util.saveBitmapToTempFile
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

fun CameraFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun CameraFragment.onBackPressed() {
    activity?.finish()
}

fun CameraFragment.initCamera(){
    binding.cameraView.apply {
        setLifecycleOwner(viewLifecycleOwner)
        addCameraListener(object : CameraListener() {
            override fun onCameraOpened(options: CameraOptions) {

            }

            override fun onPictureTaken(result: PictureResult) {
                super.onPictureTaken(result)
                if (binding.cameraView.isTakingVideo) {
                    return
                }
                onTakePictureSuccess(result)
            }

            override fun onExposureCorrectionChanged(
                newValue: Float,
                bounds: FloatArray,
                fingers: Array<PointF>?
            ) {
                super.onExposureCorrectionChanged(newValue, bounds, fingers)
            }

            override fun onZoomChanged(
                newValue: Float,
                bounds: FloatArray,
                fingers: Array<PointF>?
            ) {
                super.onZoomChanged(newValue, bounds, fingers)
            }
        })
    }
}

fun CameraFragment.captureEvent(){
    binding.btnCapture.setPreventDoubleClickScaleView {
        binding.cameraView.takePictureSnapshot()
    }
}

fun CameraFragment.onTakePictureSuccess(result : PictureResult){
    val nameImage = "${System.currentTimeMillis()}"
    val tempFile = TEMP_CAMERA


    context?.let {
        val pathFolder = "${it.filesDir.path}/$tempFile"
        if (!File(pathFolder).exists()) {
            File(pathFolder).mkdirs()
        }
        val filePath = "$pathFolder/$nameImage"
        val file = File(filePath)
        if (file.createNewFile()) {
            result.toFile(file ){ resultFile ->
                resultFile?.let {
                    safeNav(R.id.cameraFragment , CameraFragmentDirections.actionCameraFragmentToCameraImagePreviewFragment(resultFile.path))
                }
            }
        }
    }
}