package example.datlt.nextcloud.framework.presentation.listimage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import example.datlt.nextcloud.R
import example.datlt.nextcloud.database.entities.Photo
import example.datlt.nextcloud.databinding.EpoxyItemPhotoBinding
import example.datlt.nextcloud.util.setPreventDoubleClick

class PhotoAdapter(
    val onClickItem: (photo: Photo) -> Unit
) : ListAdapter<Photo, PhotoAdapter.ViewHolder>(PhotoDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot =
            LayoutInflater.from(parent.context).inflate(R.layout.epoxy_item_photo, parent, false)
        val binding = EpoxyItemPhotoBinding.bind(viewRoot)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(val binding: EpoxyItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(photo: Photo) {
            binding.apply {
                pathImage = photo.path
                isSelected = photo.isSelect
                numberWhenChoose = photo.numberWhenChoose.toString()
                mView.setPreventDoubleClick {
                    onClickItem.invoke(photo)
                }

            }
        }
    }
}