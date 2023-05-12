package example.datlt.nextcloud.framework.presentation.crop

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import example.datlt.nextcloud.R
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.framework.MainActivity.Companion.isSelectPhotoThread
import example.datlt.nextcloud.framework.presentation.crop.CropFragment.Companion.listStatePhoto
import example.datlt.nextcloud.framework.presentation.crop.adapter.ItemCropAdapter
import example.datlt.nextcloud.framework.presentation.crop.adapter.SnapHelperOneByOne
import example.datlt.nextcloud.util.*
import example.datlt.nextcloud.util.Constant.TEMP_CROP
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


fun CropFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun CropFragment.onBackPressed() {

    context?.showDialogRemoveAction(
        lifecycle = lifecycle,
        onCancel = {},
        onRemove = {
            if (isSelectPhotoThread){
                findNavController().popBackStack(R.id.selectImageFragment, false)
            }else{
                findNavController().popBackStack(R.id.cameraFragment, false)
            }
        }
    )

}

fun CropFragment.getSelectedPhoto() {
    listSelectedPhoto.clear()
    listSelectedPhoto.addAll(args.listPhoto)
    if (listSelectedPhoto.isEmpty()) {
        onBackPressed()
    } else {
        initRecyclerView()
    }
}

fun CropFragment.previousCropEvent() {
    binding.btnPrv.setPreventDoubleClickScaleView {
        if (currentItemId > 0) {
            currentItemId--
            smoothScroller.targetPosition = currentItemId
            binding.rcvCrop.layoutManager?.startSmoothScroll(smoothScroller)

        }
    }
}

fun CropFragment.nextCropEvent() {
    binding.btnNxt.setPreventDoubleClickScaleView {
        if (currentItemId < listSelectedPhoto.size - 1) {
            currentItemId++
            smoothScroller.targetPosition = currentItemId
            binding.rcvCrop.layoutManager?.startSmoothScroll(smoothScroller)
        }
    }
}

fun CropFragment.rotateLeftEvent() {
    binding.btnRotateLeft.setPreventDoubleClickScaleView {
        val viewHolder: ItemCropAdapter.ViewHolder =
            binding.rcvCrop.findViewHolderForAdapterPosition(currentItemId) as ItemCropAdapter.ViewHolder
        viewHolder.rotateLeft()
    }
}

fun CropFragment.rotateRightEvent() {
    binding.btnRotateRight.setPreventDoubleClickScaleView {
        val viewHolder: ItemCropAdapter.ViewHolder =
            binding.rcvCrop.findViewHolderForAdapterPosition(currentItemId) as ItemCropAdapter.ViewHolder
        viewHolder.rotateRight()
    }
}

fun CropFragment.initRecyclerView() {
    //create listPhoto
    createListPhotoState()
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    binding.rcvCrop.layoutManager = layoutManager
    binding.rcvCrop.adapter = adapter
    val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
    linearSnapHelper.attachToRecyclerView(binding.rcvCrop)
    adapter.submitList(listSelectedPhoto)
    binding.txvCountPhoto.text = "photo ${currentItemId + 1}/${listSelectedPhoto.size}"
    binding.rcvCrop.setOnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        binding.txvCountPhoto.text = "photo ${currentItemId + 1}/${listSelectedPhoto.size}"
    }
    smoothScroller.targetPosition = currentItemId
    binding.rcvCrop.layoutManager?.startSmoothScroll(smoothScroller)
}

fun CropFragment.deleteEvent() {
    binding.btnDelete.setPreventDoubleClickScaleView {
        if (listSelectedPhoto.size > 1) {
            listSelectedPhoto.removeAt(currentItemId)
            val newList = mutableListOf<Photo>()
            newList.addAll(listSelectedPhoto)
            adapter.submitList(newList)
            adapter.notifyDataSetChanged()
            if (currentItemId + 1 > listSelectedPhoto.size) {
                currentItemId = listSelectedPhoto.size - 1
            }
            binding.txvCountPhoto.text = "photo ${currentItemId + 1}/${listSelectedPhoto.size}"
        }
    }
}

fun CropFragment.createListPhotoState() {
    if (listStatePhoto.isEmpty()) {
        for (item in listSelectedPhoto) {
            listStatePhoto.add(
                StateData(
                    idPhoto = item.id,
                    rectCrop = getRectFull(item.path),
                    rotateDegree = 0
                )
            )
        }
    }
}

fun getRectFull(path: String): Rect {
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(path, options)
    val width = options.outWidth
    val height = options.outHeight
    return Rect(0, 0, width, height)
}

fun CropFragment.nextEvent() {
    binding.btnNext.setPreventDoubleClickScaleView {

        for (i in 0 until listSelectedPhoto.size){

            val viewHolder: ItemCropAdapter.ViewHolder? = binding.rcvCrop.findViewHolderForAdapterPosition(i) as ItemCropAdapter.ViewHolder?
            Log.d("CHECKCROP", "nextEvent: setState() ${viewHolder == null}")
            viewHolder?.setState() // bam next
        }

        //đoạn này crop ảnh
        CoroutineScope(Dispatchers.IO).launch {
            //tạo ảnh rồi lưu vào tempFolder ở đây
            context?.let {
                for (photo in listSelectedPhoto) {
                    for (state in listStatePhoto) {
                        if (photo.id == state.idPhoto) {
                            //crop ảnh ở đây
                            val nameFile = System.currentTimeMillis().toString()
                            it.cropBitmapFromPath(
                                path = photo.path,
                                state = state,
                                onCropDone = { resultBitmap ->
                                    if (resultBitmap != null) {
                                        it.saveBitmapToTempFile(
                                            bitmap = resultBitmap,
                                            nameImage = nameFile,
                                            tempFileName = TEMP_CROP
                                        )
                                    }
                                }
                            )
                            break
                        }
                    }
                }
            }
            withContext(Dispatchers.Main) {
                safeNav(
                    R.id.cropFragment, CropFragmentDirections.actionCropFragmentToSetColorFragment()
                )
            }
        }
    }
}

