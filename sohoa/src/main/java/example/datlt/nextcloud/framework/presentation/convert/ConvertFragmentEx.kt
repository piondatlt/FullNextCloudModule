package example.datlt.nextcloud.framework.presentation.convert

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.pdf.PdfDocument
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.itextpdf.text.Document
import com.itextpdf.text.Image
import com.itextpdf.text.PageSize
import com.itextpdf.text.pdf.PdfWriter
import example.datlt.nextcloud.R
import example.datlt.nextcloud.framework.MainActivity
import example.datlt.nextcloud.util.Constant.TEMP_COLOR
import example.datlt.nextcloud.util.createDocumentFile
import example.datlt.nextcloud.util.getAllFileInFolder
import example.datlt.nextcloud.util.setPreventDoubleClick
import example.datlt.nextcloud.util.setPreventDoubleClickScaleView
import example.datlt.nextcloud.util.showDialogRemoveAction
import example.datlt.nextcloud.util.showDialogSetName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun ConvertFragment.backEvent() {
    binding.btnBack.setPreventDoubleClick {
        onBackPressed()
    }
    activity?.onBackPressedDispatcher?.addCallback(this, true) {
        onBackPressed()
    }
}

fun ConvertFragment.onBackPressed() {
    context?.showDialogRemoveAction(
        lifecycle = lifecycle,
        onCancel = {},
        onRemove = {
            if (MainActivity.isSelectPhotoThread){
                findNavController().popBackStack(R.id.selectImageFragment, false)
            }else{
                findNavController().popBackStack(R.id.cameraFragment, false)
            }
        }
    )
}

fun ConvertFragment.getAllColoredPhoto() {
    if (listPhotoResult.isEmpty()) {
        context?.let {
            val listColored = it.getAllFileInFolder(TEMP_COLOR)
            listPhotoResult.addAll(listColored)
            initRecyclerView()
        }
    }
}

fun ConvertFragment.initRecyclerView() {
    binding.rcvResultImage.layoutManager = LinearLayoutManager(context)
    binding.rcvResultImage.adapter = adapter
    adapter.submitList(listPhotoResult)
}

fun ConvertFragment.nextEvent() {
    binding.btnNext.setPreventDoubleClickScaleView {
        context?.showDialogSetName(
            lifecycle = lifecycle,
            oldName = nameFile!!,
            onCancel = {
                //do nothing
            },
            onConvert = {
                nameFile = it

                //start conver

                CoroutineScope(Dispatchers.IO).launch {
                    var pathFile = ""
                    context?.let {
                        pathFile = it.createDocumentFile(nameFile.toString())
                        createPdfFromBitmaps(listPhotoResult, File(pathFile))
                    }

                    //sang màn tiếp theo
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Convert Done", Toast.LENGTH_SHORT).show()
                        //datlt sau khi convert xong
                        //back ve
                        // return the list of files (success)

                        safeNav(
                            R.id.convertFragment,
                            ConvertFragmentDirections.actionConvertFragmentToReadFileFragment(filePath = pathFile)
                        )
                    }
                }
            }
        )

    }
}

fun ConvertFragment.convertToPDF(images: List<String>, output: File) {
    val document = Document()
    PdfWriter.getInstance(document, FileOutputStream(output))
    document.open()

    val fixWidth = 400

    var bitmap: Bitmap?
    var resizeBitmap: Bitmap?

    for (image in images) {
        bitmap = Glide.with(this).asBitmap().load(image).submit().get()
        val bmWidth = bitmap.width
        val bmHeight = bitmap.height

        val fixHeight = (fixWidth.toFloat() / bmWidth.toFloat()).toFloat() * bmHeight

        resizeBitmap = Bitmap.createScaledBitmap(bitmap, fixWidth, fixHeight.toInt(), true)
        val stream = ByteArrayOutputStream()
        resizeBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val image = Image.getInstance(stream.toByteArray())
        document.add(image)
    }
    document.close()
}

fun ConvertFragment.createPdfFromBitmaps(images: List<String>, outputFile: File) {
    val doc = Document(PageSize.A4)
    PdfWriter.getInstance(doc, FileOutputStream(outputFile))
    doc.open()
    val fixWidth = 400
    var bitmap: Bitmap?
    var resizeBitmap: Bitmap?
    val pdfDocument = PdfDocument()
    for (image in images) {
        bitmap = Glide.with(this).asBitmap().load(image).submit().get()
        val bmWidth = bitmap.width
        val bmHeight = bitmap.height
        val fixHeight = (fixWidth.toFloat() / bmWidth.toFloat()) * bmHeight
        resizeBitmap = Bitmap.createScaledBitmap(bitmap, fixWidth, fixHeight.toInt(), true)
        // Create a new page
        val pageInfo =
            PdfDocument.PageInfo.Builder(resizeBitmap.width, resizeBitmap.height, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        // Scale the bitmap to fit the page
        val scaleX = pageInfo.pageWidth.toFloat() / resizeBitmap.width.toFloat()
        val scaleY = pageInfo.pageHeight.toFloat() / resizeBitmap.height.toFloat()
        val matrix = Matrix()
        matrix.setScale(scaleX, scaleY)
        val scaledBitmap = Bitmap.createBitmap(
            resizeBitmap,
            0,
            0,
            resizeBitmap.width,
            resizeBitmap.height,
            matrix,
            true
        )
        // Draw the bitmap on the page
        val canvas = page.canvas
        canvas.drawBitmap(scaledBitmap, 0f, 0f, null)
        // Finish the page
        pdfDocument.finishPage(page)
    }
    val outputStream = FileOutputStream(outputFile)
    pdfDocument.writeTo(outputStream)
    pdfDocument.close()
    outputStream.close()
}