package example.datlt.nextcloud.framework.presentation.crop.adapter

import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import example.datlt.nextcloud.R
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.ItemCropImageBinding
import example.datlt.nextcloud.framework.presentation.crop.CropFragment
import example.datlt.nextcloud.framework.presentation.crop.StateData
import example.datlt.nextcloud.framework.presentation.listimage.adapter.PhotoDiffUtils
import java.io.File


class ItemCropAdapter : ListAdapter<Photo, ItemCropAdapter.ViewHolder>(PhotoDiffUtils()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot =
            LayoutInflater.from(parent.context).inflate(R.layout.item_crop_image, parent, false)
        val binding = ItemCropImageBinding.bind(viewRoot)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        holder.setState()
    }

    inner class ViewHolder(val binding: ItemCropImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        lateinit var photo: Photo

        fun bind(photo: Photo) {
            this.photo = photo
            binding.cropImageView.apply {
                setImageUriAsync(Uri.fromFile(File(photo.path)))
                setOnSetImageUriCompleteListener { view, uri, error ->
                    val state = getStateById()
                    binding.cropImageView.cropRect = state.rectCrop
                    binding.cropImageView.rotateImage(state.rotateDegree)
                }
            }
        }

        fun rotateLeft() {
            binding.cropImageView.rotateImage(-90)
            setState()
        }

        fun rotateRight() {
            binding.cropImageView.rotateImage(90)
            setState()
        }

        fun cropImage(onCropDone : (bitmap : Bitmap) -> Unit) {
            binding.cropImageView.setOnCropImageCompleteListener { view, result ->
                onCropDone.invoke(result.bitmap)
            }
        }

        private fun getStateById(): StateData {
            for (state in CropFragment.listStatePhoto) {
                if (state.idPhoto == photo.id) {
                    return state
                    break
                }
            }
            return StateData(photo.id, Rect(), 0)
        }

        fun setState() {
            for (i in 0 until CropFragment.listStatePhoto.size) {
                if (CropFragment.listStatePhoto[i].idPhoto == photo.id) {
                    CropFragment.listStatePhoto[i].rectCrop.set(binding.cropImageView.cropRect)
                    CropFragment.listStatePhoto[i].rotateDegree =
                        binding.cropImageView.rotatedDegrees
                    break
                }
            }
        }


    }
}