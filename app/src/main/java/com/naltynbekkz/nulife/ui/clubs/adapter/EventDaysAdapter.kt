package com.naltynbekkz.nulife.ui.clubs.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Event
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.item_event_day.view.*

class EventDaysAdapter(
    private val click: (Event) -> Unit
) : RecyclerView.Adapter<EventDaysAdapter.ItemViewHolder>() {

    private var savedTasks = HashMap<String, Occurrence>()
    private var days: ArrayList<Map.Entry<Long, ArrayList<Event>>> = ArrayList()

    fun setData(events: ArrayList<Event>) {
        this.days = Convert.eventsToDays(events)
        notifyDataSetChanged()
    }

    fun setSavedData(saved: List<Occurrence>) {
        this.savedTasks.clear()
        saved.forEach {
            this.savedTasks[it.id] = it
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_event_day,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(savedTasks, days[position], click)

    override fun getItemCount() = days.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            tasks: HashMap<String, Occurrence>,
            events: Map.Entry<Long, ArrayList<Event>>,
            click: (Event) -> Unit
        ) = with(itemView) {
            date.text = Convert.timestampToDate(events.key)
            val adapter = EventsAdapter(click)
            recycler_view.adapter = adapter

            events.value.forEach {
                it.saved = tasks.containsKey(it.id)
            }

            adapter.submitList(events.value)
        }
    }

}