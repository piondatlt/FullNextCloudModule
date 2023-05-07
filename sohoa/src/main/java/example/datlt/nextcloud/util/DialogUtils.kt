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
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.DialogListFolderBinding
import example.datlt.nextcloud.databinding.DialogRemoveAllActionBinding
import example.datlt.nextcloud.databinding.DialogRenameBinding
import example.datlt.nextcloud.databinding.DialogUploadToCloudBinding
import example.datlt.nextcloud.databinding.EpoxyItemFolderPickerBinding
import example.datlt.nextcloud.framework.MainActivity


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
        edtRename.setText(currentName)
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

            val regex = Regex("[^a-zA-Z0-9_\\sđĐàáảãạăắằẵẳặâấầẩẫậéèẽẻẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵ]+")
            if (!regex.containsMatchIn(currentName)) {
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


fun Context.showDialogUploadToNextCloud(
    lifecycle: Lifecycle,
    onUpload : () -> Unit,
    onCancel : () -> Unit,
) {

    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_upload_to_cloud, null)
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
    val binding = DialogUploadToCloudBinding.bind(view)

    binding.apply {
        //init recyclerview
        btnCancel.setPreventDoubleClickScaleView {
            dialog.dismiss()
            onCancel.invoke()
        }

        btnConvert.setPreventDoubleClickScaleView {
            dialog.dismiss()
            onUpload.invoke()
        }

    }
    if (!dialog.isShowing) {
        dialog.show()
    }
}

fun Context.showDialogRemoveAction(
    lifecycle: Lifecycle,
    onRemove : () -> Unit,
    onCancel : () -> Unit,
) {

    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_remove_all_action, null)
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
    val binding = DialogRemoveAllActionBinding.bind(view)

    binding.apply {
        //init recyclerview
        btnCancel.setPreventDoubleClickScaleView {
            dialog.dismiss()
            onCancel.invoke()
        }

        btnRemove.setPreventDoubleClickScaleView {
            dialog.dismiss()
            MainActivity.isRemoveAllAction = true
            onRemove.invoke()
        }

    }
    if (!dialog.isShowing) {
        dialog.show()
    }
}


fun Context.showDialogPickFolder(
    lifecycle: Lifecycle,
    listFolder: List<String>,
    folderIsChoose: String,
    onClickFolder: (folderName: String) -> Unit,
    onCancel: () -> Unit
) {


    val listCurrentFolder = mutableListOf<FolderImage>()

    val dialog = Dialog(this)
    val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_list_folder, null)
    dialog.setContentView(view)
    dialog.setCancelable(false)
    dialog.window?.setGravity(Gravity.BOTTOM)
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
    val binding = DialogListFolderBinding.bind(view)

    val adapter = FolderAdapter {
        dialog.dismiss()
        onClickFolder(it.nameFolder)
    }

    //tạo list
    listCurrentFolder.clear()
    for(item in listFolder){
        var isSelected = false
        if (item == folderIsChoose){
            isSelected = true
        }
        listCurrentFolder.add(FolderImage(
            nameFolder =  item,
            isSelected = isSelected
        ))
    }

    adapter.submitList(listCurrentFolder)



    binding.apply {
        //init recyclerview
        rcvFolder.layoutManager = LinearLayoutManager(this@showDialogPickFolder)
        rcvFolder.adapter = adapter

        btnCancel.setPreventDoubleClickScaleView {
            dialog.dismiss()
            onCancel.invoke()
        }

    }


    if (!dialog.isShowing) {
        dialog.show()
    }
}

data class FolderImage(
    val nameFolder : String,
    val isSelected : Boolean
)

class FolderDifUtils : DiffUtil.ItemCallback<FolderImage>() {
    override fun areItemsTheSame(oldItem: FolderImage, newItem: FolderImage): Boolean {
        return oldItem.nameFolder == newItem.nameFolder
    }

    override fun areContentsTheSame(oldItem: FolderImage, newItem: FolderImage): Boolean {
        return oldItem.isSelected == newItem.isSelected
    }
}

class FolderAdapter(
    val onSelectFolder : (folder : FolderImage) -> Unit
) : ListAdapter<FolderImage , FolderAdapter.ViewHolder>(FolderDifUtils()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = EpoxyItemFolderPickerBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(val binding : EpoxyItemFolderPickerBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(folder : FolderImage){
            binding.isSelected = folder.isSelected
            binding.nameFolder = folder.nameFolder
            binding.mView.setPreventDoubleClick {
                onSelectFolder.invoke(folder)
            }

        }
    }
}