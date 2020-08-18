package com.naltynbekkz.courses.ui.deadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.core.Convert
import com.naltynbekkz.courses.databinding.ItemDeadlineDayBinding
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.timetable.model.Occurrence
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DeadlineDaysAdapter(
    private val click: (Occurrence?, Deadline) -> Unit
) : RecyclerView.Adapter<DeadlineDaysAdapter.ItemViewHolder>() {

    private var savedTasks = HashMap<String, Occurrence>()
    private var days: ArrayList<Map.Entry<Long, ArrayList<Deadline>>> = ArrayList()

    fun setData(deadlines: ArrayList<Deadline>) {
        days = deadlinesToDays(deadlines)
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
            ItemDeadlineDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(savedTasks, days[position], click)

    override fun getItemCount() = days.size

    class ItemViewHolder(val binding: ItemDeadlineDayBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            tasks: HashMap<String, Occurrence>,
            item: Map.Entry<Long, ArrayList<Deadline>>,
            click: (Occurrence?, Deadline) -> Unit
        ) {
            binding.date.text = Convert.timestampToDate(item.key)
            binding.recyclerView.adapter =
                DeadlinesAdapter(
                    item.value,
                    click,
                    tasks
                )
        }
    }

    private fun deadlinesToDays(deadlines: ArrayList<Deadline>): ArrayList<Map.Entry<Long, ArrayList<Deadline>>> {
        return ArrayList(TreeMap<Long, ArrayList<Deadline>>().apply {
            for (deadline in deadlines) {
                val day = Convert.removeHours(
                    deadline.timestamp
                ).timeInMillis / 1000
                if (containsKey(day)) {
                    this[day]!!.add(deadline)
                } else {
                    this[day] = ArrayList<Deadline>().apply {
                        add(deadline)
                    }
                }
            }
        }.entries)
    }

}