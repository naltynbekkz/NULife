package com.naltynbekkz.clubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.clubs.databinding.ItemEventBinding
import com.naltynbekkz.clubs.model.Event

class EventsAdapter(
    private val click: (String) -> Unit
) : ListAdapter<Event, EventsAdapter.EventViewHolder>(EventDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder =
        EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }


    class EventViewHolder(val binding: ItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            event: Event,
            click: (String) -> Unit
        ) {
            binding.event = event
            binding.root.setOnClickListener {
                click(binding.event!!.id)
            }
        }
    }
}
