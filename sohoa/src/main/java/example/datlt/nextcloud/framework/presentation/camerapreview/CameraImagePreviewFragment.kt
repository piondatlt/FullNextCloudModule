package example.datlt.nextcloud.framework.presentation.camerapreview


import android.util.Log
import android.view.View
import androidx.navigation.fragment.navArgs
import example.datlt.nextcloud.databinding.FragmentCameraImagePreviewBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment


class CameraImagePreviewFragment :
    BaseFragment<FragmentCameraImagePreviewBinding>(FragmentCameraImagePreviewBinding::inflate) {

    val args by navArgs<CameraImagePreviewFragmentArgs>()


    override fun init(view: View) {
        backEvent()
        nextEvent()
        initPhoto()
        deleteEvent()
        addPhotoFragment()
    }

    override fun subscribeObserver(view: View) {

    }

}