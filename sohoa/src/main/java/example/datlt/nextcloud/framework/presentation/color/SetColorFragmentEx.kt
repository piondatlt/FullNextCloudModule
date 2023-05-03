package example.datlt.nextcloud.framework.presentation.color

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.View.OnScrollChangeListener
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.pdf.PdfWriter
import example.datlt.nextcloud.R
import example.datlt.nextcloud.framework.presentation.color.SetColorFragment.Companion.listGPUIFilter
import example.datlt.nextcloud.framework.presentation.color.adapter.ItemColorAdapter
import example.datlt.nextcloud.framework.presentation.crop.CropFragment.Companion.listStatePhoto
import example.datlt.nextcloud.framework.presentation.crop.adapter.ItemCropAdapter
import example.datlt.nextcloud.framework.presentation.crop.adapter.SnapHelperOneByOne
import example.datlt.nextcloud.util.*
import example.datlt.nextcloud.util.Constant.TEMP_COLOR
import example.datlt.nextcloud.util.Constant.TEMP_CROP
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer

fun SetColorFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun SetColorFragment.onBackPressed() {
    findNavController().popBackStack(R.id.selectImageFragment, false)
}

fun SetColorFragment.getAllFileCropped() {
    CoroutineScope(Dispatchers.IO).launch {
        if (listPathCropped.isEmpty()) {
            //lấy ảnh từ file
            context?.let {
                listPathCropped.addAll(it.getAllCropBitmap())
            }
            //lấy xong thì init rcv
            withContext(Dispatchers.Main) {
                initRecyclerView()
            }
        }
    }
}

fun SetColorFragment.initRecyclerView() {
    //create list GPUI Filter
    createListFilter()
    val layoutManager = LinearLayoutManager(context)
    layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    binding.rcvSelectColor.layoutManager = layoutManager
    binding.rcvSelectColor.adapter = adapter
    val linearSnapHelper: LinearSnapHelper = SnapHelperOneByOne()
    linearSnapHelper.attachToRecyclerView(binding.rcvSelectColor)
    adapter.submitList(listPathCropped)

    binding.rcvSelectColor.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val linearLayoutManager: LinearLayoutManager =
                recyclerView.layoutManager as LinearLayoutManager

            //For finding first visible item position
            linearLayoutManager.findFirstCompletelyVisibleItemPosition()

            //For finding last visible item position
            val last = linearLayoutManager.findLastCompletelyVisibleItemPosition()
            if (last >= 0) {
                currentPosition = last
                binding.txvCountPhoto.text = "photo ${last + 1}/${listPathCropped.size}"
                onScrollToNewItem()
            }
        }
    })
    binding.txvCountPhoto.text = "photo ${currentPosition + 1}/${listPathCropped.size}"


    //initgpuiPreview
    binding.apply {

        imvNormal.filter = GPUImageFilter()
        imvBright.filter = GPUImageBrightnessFilter(0.2f)
        imvBlackWhite.filter = GPUImageGrayscaleFilter()
        imvSharp.filter = GPUImageSharpenFilter(2.0f)
    }

    onScrollToNewItem()
}

fun SetColorFragment.onScrollToNewItem() {
    val key = listPathCropped[currentPosition]
    binding.apply {
        imvNormal.setImage(File(key))
        imvBright.setImage(File(key))
        imvBlackWhite.setImage(File(key))
        imvSharp.setImage(File(key))
    }

}

fun SetColorFragment.createListFilter() {
    listGPUIFilter.clear()
    for (item in listPathCropped) {
        listGPUIFilter[item] = GPUImageFilter()
    }
}

fun SetColorFragment.normalEvent() {
    binding.btnNormal.setPreventDoubleClick {
        val key = listPathCropped[currentPosition]
        listGPUIFilter[key] = GPUImageFilter()
        val viewHolder: ItemColorAdapter.ViewHolder =
            binding.rcvSelectColor.findViewHolderForAdapterPosition(currentPosition) as ItemColorAdapter.ViewHolder
        viewHolder.changeFilter()
    }
}

fun SetColorFragment.brightEvent() {
    binding.btnBright.setPreventDoubleClick {
        val key = listPathCropped[currentPosition]
        listGPUIFilter[key] = GPUImageBrightnessFilter(0.2f)
        val viewHolder: ItemColorAdapter.ViewHolder =
            binding.rcvSelectColor.findViewHolderForAdapterPosition(currentPosition) as ItemColorAdapter.ViewHolder
        viewHolder.changeFilter()
    }
}

fun SetColorFragment.blackAndWhiteEvent() {
    binding.btnBlackWhite.setPreventDoubleClick {
        val key = listPathCropped[currentPosition]
        listGPUIFilter[key] = GPUImageGrayscaleFilter()
        val viewHolder: ItemColorAdapter.ViewHolder =
            binding.rcvSelectColor.findViewHolderForAdapterPosition(currentPosition) as ItemColorAdapter.ViewHolder
        viewHolder.changeFilter()
    }

}

fun SetColorFragment.sharpEvent() {
    binding.btnSharp.setPreventDoubleClick {
        val key = listPathCropped[currentPosition]
        listGPUIFilter[key] = GPUImageSharpenFilter(2.0f)
        val viewHolder: ItemColorAdapter.ViewHolder =
            binding.rcvSelectColor.findViewHolderForAdapterPosition(currentPosition) as ItemColorAdapter.ViewHolder
        viewHolder.changeFilter()
    }
}

fun SetColorFragment.nextEvent() {
    binding.btnNext.setPreventDoubleClickScaleView {
        //lưu ảnh với filter đã cho
        CoroutineScope(Dispatchers.IO).launch {
            context?.let {
                //xoá các file cũ đi
                val gpui = GPUImage(it)
                for (path in listPathCropped){

                    gpui.setFilter(listGPUIFilter[path])
                    val bitmap = gpui.getBitmapWithFilterApplied(BitmapFactory.decodeFile(path) , true)
                    it.saveBitmapToTempFile(
                        bitmap = bitmap,
                        nameImage = System.currentTimeMillis().toString(),
                        tempFileName = TEMP_COLOR
                    )
                }
            }

            //convert

            //sang màn tiếp theo
            withContext(Dispatchers.Main) {
                safeNav(R.id.setColorFragment , R.id.action_setColorFragment_to_convertFragment)
            }
        }
    }
}

fun SetColorFragment.convertToPDF(images: List<String>, output: File) {
    val document = Document()
    PdfWriter.getInstance(document, FileOutputStream(output))
    document.open()

    for (image in images) {
        val bitmap = Glide.with(this).asBitmap().load(image).submit().get()
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        document.add(image)
    }
    document.close()
}