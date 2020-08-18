package com.naltynbekkz.food.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.food.R
import com.naltynbekkz.food.databinding.ItemCafeBinding
import com.naltynbekkz.food.model.Cafe

class CafeAdapter(
    private val click: (String) -> Unit
) : ListAdapter<Cafe, CafeAdapter.CafeViewHolder>(
    CafeDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder =
        CafeViewHolder(
            ItemCafeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) =
        holder.bind(getItem(position), click)

    class CafeViewHolder(val binding: ItemCafeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cafe: Cafe, click: (String) -> Unit) {
            binding.cafe = cafe
            binding.root.setOnClickListener {
                click(cafe.id)
            }
            binding.rating.isEnabled = false
            binding.time.setTextAppearance(if (cafe.isOpen()) R.style.OpenedText else R.style.ClosedText)
        }
    }

}


private class CafeDiffCallback : DiffUtil.ItemCallback<Cafe>() {

    override fun areItemsTheSame(oldItem: Cafe, newItem: Cafe): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Cafe, newItem: Cafe): Boolean {
        return oldItem == newItem
    }
}