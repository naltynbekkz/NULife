package com.naltynbekkz.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.databinding.ItemTaskDayBinding
import com.naltynbekkz.timetable.model.Occurrence
import java.util.*
import kotlin.collections.ArrayList

class TaskDaysAdapter : RecyclerView.Adapter<TaskDaysAdapter.ItemViewHolder>() {

    var tasks =
        ArrayList<Map.Entry<Long, ArrayList<Occurrence>>>()

    fun setData(tasks: List<Occurrence>) {
        this.tasks = tasksToDays(tasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        ItemTaskDayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val a = tasks[position]
        holder.bind(a.key, a.value)
    }

    override fun getItemCount() = tasks.size

    class ItemViewHolder(val binding: ItemTaskDayBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            day: Long,
            tasks: ArrayList<Occurrence>
        ) = with(itemView) {
            binding.date.text = Convert.timestampToDate(day)
            binding.recyclerView.adapter = TasksAdapter(tasks)
        }
    }

    private fun tasksToDays(tasks: List<Occurrence>): ArrayList<Map.Entry<Long, ArrayList<Occurrence>>> {
        return ArrayList(TreeMap<Long, ArrayList<Occurrence>>().apply {
            for (task in tasks) {
                val day = Convert.removeHours(
                    task.start
                ).timeInMillis / 1000
                if (containsKey(day)) {
                    this[day]!!.add(task)
                } else {
                    this[day] = ArrayList<Occurrence>().apply {
                        add(task)
                    }
                }
            }
        }.entries)
    }

}