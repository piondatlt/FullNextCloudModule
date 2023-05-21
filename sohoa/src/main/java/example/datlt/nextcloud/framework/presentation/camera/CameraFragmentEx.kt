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
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.framework.presentation.camerapreview.CameraImagePreviewFragmentDirections
import example.datlt.nextcloud.util.Constant.TEMP_CAMERA
import example.datlt.nextcloud.util.saveBitmapToTempFile
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pion.tech.magnifier2.framework.customview.viewinterface.CustomSeekbarInterface
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

fun CameraFragment.initCamera() {
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

fun CameraFragment.captureEvent() {
    binding.btnCapture.setPreventDoubleClickScaleView {
        binding.cameraView.takePictureSnapshot()
    }
}

fun CameraFragment.onTakePictureSuccess(result: PictureResult) {
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
            result.toFile(file) { resultFile ->
                resultFile?.let {
                    safeNav(
                        R.id.cameraFragment,
                        CameraFragmentDirections.actionCameraFragmentToCameraImagePreviewFragment(resultFile.path)
                    )
                }
            }
        }
    }
}

fun CameraFragment.nextEvent() {
    context?.let {
        val tempFile = TEMP_CAMERA
        val pathFolder = "${it.filesDir.path}/$tempFile"
        val folder = File(pathFolder)

        if (folder.exists()) {
            val listFile = folder.list()
            if (listFile.isNullOrEmpty()) {
                binding.btnNext.text = "${getString(R.string.next)} (0)"
            } else {
                binding.btnNext.text = "${getString(R.string.next)} (${listFile.size})"
            }
        } else {
            binding.btnNext.text = "${getString(R.string.next)} (0)"
        }

        binding.btnNext.setPreventDoubleClickScaleView {
            context?.let {
                val listSelectedPhoto = mutableListOf<Photo>()
                val pathFolder = "${it.filesDir.path}/$TEMP_CAMERA"
                val folder = File(pathFolder)
                var count = 0
                folder.listFiles()?.let { listFile ->
                    for (item in listFile) {
                        listSelectedPhoto.add(
                            Photo(
                                id = System.currentTimeMillis(),
                                name = item.name,
                                nameFolder = TEMP_CAMERA,
                                path = item.path,
                                pathFolder = pathFolder,
                                typeMedia = "png",
                                isSelect = true,
                                numberWhenChoose = count
                            )
                        )
                        count++
                    }
                }
                if (listSelectedPhoto.isNotEmpty()) {
                    //sang man tiep theo
                    safeNav(
                        R.id.cameraFragment,
                        CameraFragmentDirections.actionCameraFragmentToCropFragment(listPhoto = listSelectedPhoto.toTypedArray())
                    )
                } else {
                    //khong co anh de convert
                }
            }
        }

    }
}

fun CameraFragment.initZoom() {
    binding.sbZoom.setProgressChangeListener(object : CustomSeekbarInterface {
        override fun onStartToTouch() {
            //do nothing
        }

        override fun onProgressChange(progress: Int) {
            //call when zoom change
            binding.cameraView.zoom = progress.toFloat() / 100
        }

        override fun onStopToTouch() {
            //do nothing
        }
    })
}

fun CameraFragment.initBrightness() {
    binding.sbBright.setProgress(50)
    binding.sbBright.setProgressChangeListener(object : CustomSeekbarInterface {
        override fun onStartToTouch() {
            //do nothing
        }

        override fun onProgressChange(progress: Int) {
            //call when brightness change
            binding.cameraView.cameraOptions?.let { cameraOptions ->
                val max = cameraOptions.exposureCorrectionMaxValue
                val min = cameraOptions.exposureCorrectionMinValue
                binding.cameraView.exposureCorrection = ((max * progress) / 50) - max
            }
        }

        override fun onStopToTouch() {
            //do nothing
        }
    })
}

