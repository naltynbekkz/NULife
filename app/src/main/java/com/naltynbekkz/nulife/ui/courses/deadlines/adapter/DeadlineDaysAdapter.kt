package com.naltynbekkz.nulife.ui.courses.deadlines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Deadline
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.item_deadline_day.view.*

class DeadlineDaysAdapter(
    private val click: (Occurrence?, Deadline) -> Unit
) : RecyclerView.Adapter<DeadlineDaysAdapter.ItemViewHolder>() {

    private var savedTasks = HashMap<String, Occurrence>()
    private var days: ArrayList<Map.Entry<Long, ArrayList<Deadline>>> = ArrayList()

    fun setData(deadlines: ArrayList<Deadline>) {
        days = Convert.deadlinesToDays(deadlines)
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
                R.layout.item_deadline_day,
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
            item: Map.Entry<Long, ArrayList<Deadline>>,
            click: (Occurrence?, Deadline) -> Unit
        ) =
            with(itemView) {
                date.text = Convert.timestampToDate(item.key)
                recycler_view.adapter =
                    DeadlinesAdapter(
                        item.value,
                        click,
                        tasks
                    )
            }
    }

}