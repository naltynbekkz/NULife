package com.naltynbekkz.nulife.ui.clubs.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemEventDayBinding
import com.naltynbekkz.nulife.model.Event
import com.naltynbekkz.nulife.util.Convert

class EventDaysAdapter(
    private val click: (String) -> Unit
) : ListAdapter<Map.Entry<Long, ArrayList<Event>>, EventDaysAdapter.ItemViewHolder>(EventDaysDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemEventDayBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(getItem(position), click)

    class ItemViewHolder(val binding: ItemEventDayBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            events: Map.Entry<Long, ArrayList<Event>>,
            click: (String) -> Unit
        ) {
            binding.date.text = Convert.timestampToDate(events.key)
            val adapter = EventsAdapter(click)
            binding.recyclerView.adapter = adapter
            adapter.submitList(events.value)
        }
    }

}

class EventDaysDiffCallback : DiffUtil.ItemCallback<Map.Entry<Long, ArrayList<Event>>>() {

    override fun areItemsTheSame(
        oldItem: Map.Entry<Long, ArrayList<Event>>,
        newItem: Map.Entry<Long, ArrayList<Event>>
    ): Boolean {
        return oldItem.key == newItem.key
    }

    override fun areContentsTheSame(
        oldItem: Map.Entry<Long, ArrayList<Event>>,
        newItem: Map.Entry<Long, ArrayList<Event>>
    ): Boolean {
        return oldItem.value == newItem.value
    }
}