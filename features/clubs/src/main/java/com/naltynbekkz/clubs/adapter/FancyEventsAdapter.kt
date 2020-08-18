package com.naltynbekkz.clubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.clubs.databinding.ItemEventFancyBinding
import com.naltynbekkz.clubs.model.Event

class FancyEventsAdapter(
    private val click: (String) -> Unit
) : ListAdapter<Event, FancyEventsAdapter.FancyEventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FancyEventViewHolder(
        ItemEventFancyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FancyEventViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    class FancyEventViewHolder(private val binding: ItemEventFancyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(event: Event, click: (String) -> Unit) {
            binding.root.setOnClickListener {
                click(event.id)
            }
            binding.event = event
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {

    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}