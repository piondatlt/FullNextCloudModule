package example.datlt.nextcloud.util

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.recyclerview.widget.LinearLayoutManager
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.DialogListFolderBinding
import example.datlt.nextcloud.databinding.DialogRenameBinding

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


fun Context.showDialogSetName(
    lifecycle: Lifecycle,
    oldName : String,
    onConvert : (newName : String) -> Unit,
    onCancel : () -> Unit,
) {

    var currentName = oldName

    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_rename, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window?.setGravity(Gravity.CENTER)
    dialog.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
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
    val binding = DialogRenameBinding.bind(view)

    binding.apply {
        //init recyclerview
        btnCancel.setPreventDoubleClickScaleView {
            dialog.dismiss()
            onCancel.invoke()
        }

        btnConvert.setPreventDoubleClickScaleView {
            dialog.dismiss()

            if (edtRename.text.toString().isNotEmpty()){
                currentName = edtRename.text.toString()
            }


            if (currentName.matches(Regex(".*\\.pdf$")) || currentName.matches(Regex(".*\\.PDF$"))) {
                // Tên file hợp lệ
                onConvert.invoke(currentName)
            } else {
                // Tên file không hợp lệ
                Toast.makeText(this@showDialogSetName , "Name wrong format" , Toast.LENGTH_SHORT).show()
            }
        }

    }
    if (!dialog.isShowing) {
        dialog.show()
    }
}
