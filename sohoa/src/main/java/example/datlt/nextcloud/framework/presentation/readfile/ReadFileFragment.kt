package example.datlt.nextcloud.framework.presentation.readfile

import android.view.View
import androidx.navigation.fragment.navArgs
import example.datlt.nextcloud.databinding.FragmentReadFileBinding
import example.datlt.nextcloud.framework.presentation.common.BaseFragment


class ReadFileFragment : BaseFragment<FragmentReadFileBinding>(FragmentReadFileBinding::inflate) {

    val args by navArgs<ReadFileFragmentArgs>()
    var filePath = ""


    override fun init(view: View) {
        backEvent()
        showPdf()
        deleteEvent()
        shareEvent()
        uploadEvent()
    }

    override fun subscribeObserver(view: View) {

    }

}