package com.naltynbekkz.nulife.ui.timetable.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.item_task_day.view.*

class TaskDaysAdapter : RecyclerView.Adapter<TaskDaysAdapter.ItemViewHolder>() {

    var tasks: ArrayList<Map.Entry<Long, ArrayList<Occurrence>>> = ArrayList()

    fun setData(tasks: List<Occurrence>) {
        this.tasks = Convert.tasksToDays(tasks)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_task_day,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val a = tasks[position]
        holder.bind(a.key, a.value)
    }

    override fun getItemCount() = tasks.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(
            day: Long,
            tasks: ArrayList<Occurrence>
        ) = with(itemView) {
            date.text = Convert.timestampToDate(day)
            recycler_view.adapter = TasksAdapter(tasks)
        }
    }
}