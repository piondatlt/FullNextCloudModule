package example.datlt.nextcloud.framework.presentation.convert

import android.view.View
import example.datlt.nextcloud.databinding.FragmentConvertBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment
import example.datlt.nextcloud.framework.presentation.convert.adapter.PhotoResultAdapter


class ConvertFragment : BaseFragment<FragmentConvertBinding>(FragmentConvertBinding::inflate) {


    val adapter = PhotoResultAdapter()

    var listPhotoResult = mutableListOf<String>()

    var nameFile : String? = null

    override fun init(view: View) {
        if (nameFile == null){
            nameFile ="Convert_${System.currentTimeMillis()}"
        }
        backEvent()
        getAllColoredPhoto()
        nextEvent()
    }

    override fun subscribeObserver(view: View) {

    }

}