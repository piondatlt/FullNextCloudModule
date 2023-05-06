package example.datlt.nextcloud.framework.presentation.readfile

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import example.datlt.nextcloud.R
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import example.datlt.nextcloud.util.showDialogRemoveAction
import example.datlt.nextcloud.util.showDialogUploadToNextCloud
import java.io.File

fun ReadFileFragment.backEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {

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
        context?.showDialogRemoveAction(
            lifecycle = lifecycle,
            onCancel = {
                       //do nothing
            },
            onRemove = {
                File(filePath).deleteOnExit()
                activity?.finish()
            },
        )
    }
}

fun ReadFileFragment.shareEvent(){
    binding.btnShare.setPreventDoubleClickScaleView {
        context?.let {
            val pdfFile = File(filePath)
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            val uri = FileProvider.getUriForFile(it, "org.nextcloud.files" , pdfFile)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            startActivity(Intent.createChooser(intent, "Share PDF file"))
        }
    }
}

fun ReadFileFragment.uploadEvent(){
    binding.btnUpload.setPreventDoubleClickScaleView {
        context?.showDialogUploadToNextCloud(
            lifecycle = lifecycle,
            onCancel = {
                //do nothing
            },
            onUpload = {
                val data = Intent()
                data.putExtra("path", filePath)
                data.putExtra("basePath", File(filePath).parent)
                activity?.let {
                    it.setResult(Activity.RESULT_OK, data)
                    it.finish()
                }
            }
        )
    }
}