package example.datlt.nextcloud.framework.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import example.datlt.nextcloud.R
import example.datlt.nextcloud.databinding.ItemFileCreatedBinding
import example.datlt.nextcloud.framework.presentation.color.adapter.StringDiffUtils
import example.datlt.nextcloud.util.setPreventDoubleClick
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ItemCreatedAdapter(
    val onClickItem : (pathFile : String) -> Unit
) : ListAdapter<String , ItemCreatedAdapter.ViewHolder>(StringDiffUtils()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewRoot =
            LayoutInflater.from(parent.context).inflate(R.layout.item_file_created, parent, false)
        val binding = ItemFileCreatedBinding.bind(viewRoot)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


    inner class ViewHolder(val binding : ItemFileCreatedBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(pathFile: String){
            val file = File(pathFile)
            val name = file.name
            val detail = "Date created : ${convertDate(file.lastModified())}"

            binding.txvName.text = name
            binding.txvDetail.text = detail
            binding.mView.setPreventDoubleClick {
                onClickItem.invoke(pathFile)
            }

        }

        private fun convertDate(date : Long) : String{
            val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            return dateFormat.format(Date(date))
        }
    }


}