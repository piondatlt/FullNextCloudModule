package example.datlt.nextcloud.framework.presentation.color.adapter

import androidx.recyclerview.widget.DiffUtil

class StringDiffUtils : DiffUtil.ItemCallback<String>() {

    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}