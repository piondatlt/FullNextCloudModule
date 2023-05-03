package example.datlt.nextcloud.framework.presentation.convert.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.ItemResultPhotoBinding
import example.datlt.nextcloud.framework.presentation.color.adapter.StringDiffUtils

class PhotoResultAdapter : ListAdapter<String, PhotoResultAdapter.ViewHolder>(StringDiffUtils()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot =
            LayoutInflater.from(parent.context).inflate(R.layout.item_result_photo, parent, false)
        val binding = ItemResultPhotoBinding.bind(viewRoot)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ViewHolder(val binding: ItemResultPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(path: String) {
            Glide.with(binding.imvResultPhoto).load(path).into(binding.imvResultPhoto)
        }
    }

}