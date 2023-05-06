package example.datlt.nextcloud.framework.presentation.camera


import android.view.View
import example.datlt.nextcloud.databinding.FragmentCameraBinding
import example.datlt.nextcloud.framework.MainActivity
import example.datlt.nextcloud.framework.presentation.color.SetColorFragment
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.crop.CropFragment
import example.datlt.nextcloud.util.Constant
import example.datlt.nextcloud.util.removeTempFile

class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {

    var isInit = true

    override fun init(view: View) {

        //khi init hoặc bị remove
        context?.let {
            if (isInit || MainActivity.isRemoveAllAction){
                isInit = false
                MainActivity.isRemoveAllAction = false
                it.removeTempFile(Constant.TEMP_CAMERA)
                it.removeTempFile(Constant.TEMP_CROP)
                it.removeTempFile(Constant.TEMP_COLOR)
                CropFragment.listStatePhoto.clear()
                SetColorFragment.listGPUIFilter.clear()
            }
        }


        backEvent()
        initCamera()
        captureEvent()
        nextEvent()
    }

    override fun subscribeObserver(view: View) {

    }

}