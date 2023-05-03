package example.datlt.nextcloud.framework.presentation.readfile

import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import example.datlt.nextcloud.R
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import java.io.File

fun ReadFileFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun ReadFileFragment.onBackPressed() {
    findNavController().popBackStack(R.id.homeFragment, false)
}

fun ReadFileFragment.showPdf(){
    filePath = args.filePath
    binding.pdfView.fromFile(File(filePath))
        .defaultPage(0)
        .spacing(20)
        .enableSwipe(true)
        .swipeHorizontal(false)
        .onLoad { /* PDF loaded successfully */
        }
        .onError {
            Toast.makeText(context , "Open Error" , Toast.LENGTH_SHORT).show()
        }
        .load()
}

fun ReadFileFragment.deleteEvent(){
    binding.btnDelete.setPreventDoubleClickScaleView {
        File(filePath).deleteOnExit()
        onBackPressed()
    }
}

fun ReadFileFragment.shareEvent(){
    binding.btnShare.setPreventDoubleClickScaleView {

    }
}

fun ReadFileFragment.uploadEvent(){
    binding.btnUpload.setPreventDoubleClickScaleView {

    }
}