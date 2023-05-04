package example.datlt.nextcloud.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.recyclerview.widget.LinearLayoutManager
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.DialogListFolderBinding

fun Context.showDialogPickFolder(
    lifecycle: Lifecycle,
    listFolder: List<String>,
    folderIsChoose: String,
    onClickFolder: (folderName: String) -> Unit,
    onCancel: () -> Unit
) {
    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_list_folder, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.window?.setGravity(Gravity.BOTTOM)
    dialog.window?.setLayout(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    lifecycle.addObserver(LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_PAUSE -> {
                dialog.dismiss()
            }
            else -> {

            }
        }
    })
    val binding = DialogListFolderBinding.bind(view)
    binding.apply {
        //init recyclerview
        rcvFolder.layoutManager = LinearLayoutManager(this@showDialogPickFolder)


    }
    if (!dialog.isShowing) {
        dialog.show()
    }
}
