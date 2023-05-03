package example.datlt.nextcloud.framework.presentation.listimage

import android.view.View
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import example.datlt.nextcloud.R
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.framework.presentation.crop.CropFragment.Companion.listStatePhoto
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun SelectImageFragment.backEvent() {
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
    binding.btnBack.setPreventDoubleClickScaleView {
        onBackPressed()
    }
}

fun SelectImageFragment.onBackPressed() {
    findNavController().popBackStack()
}

fun SelectImageFragment.getListFolder() {
    listFolderPhoto.clear()
    for (item in listAllPhoto) {
        if (!listFolderPhoto.contains(item.nameFolder)) {
            listFolderPhoto.add(item.nameFolder)
        }
    }
    listFolderPhoto.sortBy { it }
}

fun SelectImageFragment.initRecyclerView() {
    binding.rcvListPhoto.layoutManager = GridLayoutManager(context!!, 3)
    binding.rcvListPhoto.adapter = adapter
}

fun SelectImageFragment.setListToAdapter() {
    CoroutineScope(Dispatchers.Main).launch {
        adapter.submitList(listPhotoDisplay)
    }
}

fun SelectImageFragment.clickItem(photo: Photo) {
    //check xem da co trong list selcted chua
    var checkIfSelected = false
    for (item in listSelectedPhoto) {
        if (item.id == photo.id) {
            checkIfSelected = true
            break
        }
    }
    if (checkIfSelected) {
        //da co trong list
        //bo ra khoi list
        listSelectedPhoto.removeIf { it.id == photo.id }
        //update lai so thu tu cua item
        for (i in 0 until listSelectedPhoto.size) {
            val oldItem = listSelectedPhoto[i]
            val newItem = Photo(
                id = oldItem.id,
                name = oldItem.name,
                nameFolder = oldItem.nameFolder,
                path = oldItem.path,
                pathFolder = oldItem.pathFolder,
                typeMedia = oldItem.typeMedia,
                isSelect = true,
                numberWhenChoose = i + 1
            )
            listSelectedPhoto[i] = newItem
        }
    } else {
        //chua co trong list
        //them vao list
        //tao mot item moi
        val newItem = Photo(
            id = photo.id,
            name = photo.name,
            nameFolder = photo.nameFolder,
            path = photo.path,
            pathFolder = photo.pathFolder,
            typeMedia = photo.typeMedia,
            isSelect = true,
            numberWhenChoose = listSelectedPhoto.size + 1
        )
        listSelectedPhoto.add(newItem)
    }

    //update lai list display
    val newList = mutableListOf<Photo>()
    for (i in 0 until listPhotoDisplay.size) {

        var isSelected = false
        var numberChosen = 0
        for (j in 0 until listSelectedPhoto.size) {
            if (listPhotoDisplay[i].id == listSelectedPhoto[j].id) {
                isSelected = true
                numberChosen = listSelectedPhoto[j].numberWhenChoose
                break
            }
        }
        val newItem = Photo(
            id = listPhotoDisplay[i].id,
            name = listPhotoDisplay[i].name,
            nameFolder = listPhotoDisplay[i].nameFolder,
            path = listPhotoDisplay[i].path,
            pathFolder = listPhotoDisplay[i].pathFolder,
            typeMedia = listPhotoDisplay[i].typeMedia,
            isSelect = isSelected,
            numberWhenChoose = numberChosen
        )
        newList.add(newItem)
    }
    adapter.submitList(newList)
    //update next button
    if (listSelectedPhoto.size > 0) {
        binding.btnNext.text = "Next (${listSelectedPhoto.size})"
        binding.btnNext.visibility = View.VISIBLE
    } else {
        binding.btnNext.visibility = View.GONE
    }

}

fun SelectImageFragment.nextEvent() {
    binding.btnNext.setPreventDoubleClickScaleView {
        if (listSelectedPhoto.size > 0) {
            safeNav(
                R.id.selectImageFragment,
                SelectImageFragmentDirections.actionSelectImageFragmentToCropFragment(
                    listPhoto = listSelectedPhoto.toTypedArray()
                ),
            )
        }
    }

}