package example.datlt.nextcloud.framework.presentation.camerapreview

import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import example.datlt.nextcloud.R
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.util.Constant.TEMP_CAMERA
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import java.io.File

fun CameraImagePreviewFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun CameraImagePreviewFragment.onBackPressed() {
    findNavController().popBackStack()
}

fun CameraImagePreviewFragment.nextEvent(){
    context?.let {
        val tempFile = TEMP_CAMERA
        val pathFolder = "${it.filesDir.path}/$tempFile"
        val folder = File(pathFolder)
        folder.list()?.let {listFile ->
            binding.btnNext.text = "${getString(R.string.next)} (${listFile.size})"
        }
    }

    binding.btnNext.setPreventDoubleClick {

        context?.let {
            val listSelectedPhoto = mutableListOf<Photo>()
            val pathFolder = "${it.filesDir.path}/$TEMP_CAMERA"
            val folder = File(pathFolder)
            var count = 0
            folder.listFiles()?.let {listFile ->
                for (item in listFile){
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
            //sang man tiep theo
            safeNav(R.id.cameraImagePreviewFragment , CameraImagePreviewFragmentDirections.actionCameraImagePreviewFragmentToCropFragment(listPhoto = listSelectedPhoto.toTypedArray()))
        }
    }
}

fun CameraImagePreviewFragment.initPhoto(){
    val pathPhoto = args.filePath
    context?.let { Glide.with(it).load(pathPhoto).into(binding.photoView) }
}

fun CameraImagePreviewFragment.deleteEvent(){
    binding.btnDelete.setPreventDoubleClickScaleView {
        val pathPhoto = args.filePath
        File(pathPhoto).delete()
        findNavController().popBackStack()
    }
}

fun CameraImagePreviewFragment.addPhotoFragment(){
    binding.btnAddImage.setPreventDoubleClickScaleView {
        findNavController().popBackStack()
    }
}
