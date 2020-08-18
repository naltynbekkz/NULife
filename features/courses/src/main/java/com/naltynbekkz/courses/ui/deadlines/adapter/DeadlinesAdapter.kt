package com.naltynbekkz.courses.ui.deadlines.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemDeadlineBinding
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.timetable.model.Occurrence
import java.util.*

class DeadlinesAdapter(
    private var deadlines: ArrayList<Deadline>,
    private val click: (Occurrence?, Deadline) -> Unit,
    private val tasks: HashMap<String, Occurrence>
) :
    RecyclerView.Adapter<DeadlinesAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemDeadlineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount() = deadlines.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(tasks[deadlines[position].id], deadlines[position], click)

    class ItemViewHolder(val binding: ItemDeadlineBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            task: Occurrence?,
            deadline: Deadline,
            click: (Occurrence?, Deadline) -> Unit
        ) {
            binding.task = task
            binding.deadline = deadline
            binding.root.setOnClickListener {
                click(task, deadline)
            }
        }
    }

}