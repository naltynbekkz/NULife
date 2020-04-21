package com.naltynbekkz.nulife.ui.clubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemHeadBinding
import com.naltynbekkz.nulife.model.Head

class HeadsAdapter : ListAdapter<Head, HeadsAdapter.ItemViewHolder>(HeadDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemHeadBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ItemViewHolder(val binding: ItemHeadBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            head: Head
        ) {
            binding.head = head
        }
    }

}

private class HeadDiffCallback : DiffUtil.ItemCallback<Head>() {

    override fun areItemsTheSame(oldItem: Head, newItem: Head): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Head, newItem: Head): Boolean {
        return oldItem == newItem
    }
}
