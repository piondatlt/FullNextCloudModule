package example.datlt.nextcloud.framework.presentation.home


import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.FragmentHomeBinding
import example.datlt.nextcloud.framework.presentation.color.SetColorFragment
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.crop.CropFragment
import example.datlt.nextcloud.framework.presentation.home.adapter.ItemCreatedAdapter
import example.datlt.nextcloud.util.Constant.TEMP_CAMERA
import example.datlt.nextcloud.util.Constant.TEMP_COLOR
import example.datlt.nextcloud.util.Constant.TEMP_CROP
import example.datlt.nextcloud.util.removeTempFile


class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    var resultRequestManageStoragePermission =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    var adapter = ItemCreatedAdapter {
        onClickItemCreated(it)
    }

    override fun init(view: View) {

        backEvent()

        //xin quyen
        if (!checkPermission()) {
            requestPermission()
        } else {
            //do nothing
        }

        photoToPdfEvent()
        cameraToPdfEvent()
        nextCloudEvent()
        getAllCreatedFile()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            100 -> {
                if (checkCameraPermission()) {
                    safeNav(R.id.homeFragment, R.id.action_homeFragment_to_cameraFragment)
                }
            }
            else -> {

            }
        }
    }

    override fun subscribeObserver(view: View) {

    }

}