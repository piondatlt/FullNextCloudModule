package example.datlt.nextcloud.framework.presentation.color


import android.view.View
import androidx.navigation.fragment.navArgs
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.FragmentSetColorBinding
import example.datlt.nextcloud.framework.presentation.color.adapter.ItemColorAdapter
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.crop.CropFragmentArgs
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter


class SetColorFragment : BaseFragment<FragmentSetColorBinding>(FragmentSetColorBinding::inflate) {

    val listPathCropped = mutableListOf<String>()
    val adapter = ItemColorAdapter()
    var currentPosition = 0


    override fun init(view: View) {



        backEvent()
        getAllFileCropped()

        //set filter
        normalEvent()
        brightEvent()
        blackAndWhiteEvent()
        sharpEvent()
        nextEvent()


    }

    override fun subscribeObserver(view: View) {

    }

    companion object {
        val listGPUIFilter = hashMapOf<String , GPUImageFilter>()
    }

}