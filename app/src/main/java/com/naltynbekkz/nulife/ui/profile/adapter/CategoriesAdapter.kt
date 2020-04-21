package com.naltynbekkz.nulife.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemCategoryBinding
import com.naltynbekkz.nulife.model.Category

class CategoriesAdapter(
    private val follow: (String, Boolean) -> Unit
) : ListAdapter<Category, CategoriesAdapter.ItemViewHolder>(
    CategoryDiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        return ItemViewHolder(
            ItemCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position), follow)
    }

    class ItemViewHolder(val binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            category: Category,
            follow: (String, Boolean) -> Unit
        ) {
            binding.category = category
            binding.root.setOnClickListener {
                if (binding.category!!.following != null) {
                    follow(binding.category!!.id, binding.category!!.following!!)
                    binding.category = binding.category!!.apply {
                        following = null
                    }
                }
            }
        }
    }

}


private class CategoryDiffCallback : DiffUtil.ItemCallback<Category>() {

    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem == newItem
    }
}