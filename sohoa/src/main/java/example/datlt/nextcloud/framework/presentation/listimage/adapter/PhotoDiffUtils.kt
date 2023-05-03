package example.datlt.nextcloud.framework.presentation.listimage.adapter

import androidx.recyclerview.widget.DiffUtil
import example.datlt.nextcloud.database.entities.Photo

class PhotoDiffUtils : DiffUtil.ItemCallback<Photo>() {

    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}