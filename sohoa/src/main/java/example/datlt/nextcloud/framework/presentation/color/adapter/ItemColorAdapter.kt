package example.datlt.nextcloud.framework.presentation.color.adapter

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.ItemColorImageBinding
import example.datlt.nextcloud.framework.presentation.color.SetColorFragment
import jp.co.cyberagent.android.gpuimage.GPUImage
import java.io.File

class ItemColorAdapter : ListAdapter<String, ItemColorAdapter.ViewHolder>(StringDiffUtils()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot =
            LayoutInflater.from(parent.context).inflate(R.layout.item_color_image, parent, false)
        val binding = ItemColorImageBinding.bind(viewRoot)
        return ViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class ViewHolder(
        val binding: ItemColorImageBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {

        val gpui = GPUImage(context)
        var pathPhoto = ""

        fun bind(pathPhoto: String) {
            this.pathPhoto = pathPhoto
            val bitmap = BitmapFactory.decodeFile(pathPhoto)
            SetColorFragment.listGPUIFilter[pathPhoto]?.let {
                gpui.setFilter(it)
            }
            Glide.with(binding.imvPreview).load(gpui.getBitmapWithFilterApplied(bitmap , true)).into(binding.imvPreview)
        }

        fun changeFilter(){
            val bitmap = BitmapFactory.decodeFile(pathPhoto)
            SetColorFragment.listGPUIFilter[pathPhoto]?.let {
                gpui.setFilter(it)
            }
            Glide.with(binding.imvPreview).load(gpui.getBitmapWithFilterApplied(bitmap , true)).into(binding.imvPreview)
        }
    }
}