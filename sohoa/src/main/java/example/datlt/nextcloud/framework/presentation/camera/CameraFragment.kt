package example.datlt.nextcloud.framework.presentation.camera


import android.view.View
import example.datlt.nextcloud.databinding.FragmentCameraBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment


class CameraFragment : BaseFragment<FragmentCameraBinding>(FragmentCameraBinding::inflate) {


    override fun init(view: View) {
        backEvent()
        initCamera()
        captureEvent()
    }

    override fun subscribeObserver(view: View) {

    }

}