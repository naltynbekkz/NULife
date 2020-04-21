package com.naltynbekkz.nulife.ui.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemAssociateBinding
import com.naltynbekkz.nulife.model.Associate

class AssociatesAdapter(
    private val associates: ArrayList<Associate>,
    private val click: (Associate) -> Unit
) : RecyclerView.Adapter<AssociatesAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemAssociateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(associates[position], click)
    }

    override fun getItemCount() = associates.size

    inner class ItemViewHolder(private val binding: ItemAssociateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            associate: Associate,
            click: (Associate) -> Unit
        ) {
            binding.associate = associate
            binding.layout.setOnClickListener {
                click(associate)
            }
        }
    }
}