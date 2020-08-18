package com.naltynbekkz.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.timetable.databinding.ItemRoutineBinding
import com.naltynbekkz.timetable.databinding.ItemTaskBinding

class WeekdayAdapter(
    private val taskClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit,
    private val routineClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var occurrences: List<com.naltynbekkz.timetable.model.Occurrence> = ArrayList()

    companion object {
        const val ROUTINE = 0
        const val TASK = 1
    }

    fun setOccurrences(occurrences: List<com.naltynbekkz.timetable.model.Occurrence>) {
        this.occurrences = ArrayList(occurrences).apply {
            sortWith(Comparator { o1, o2 ->
                (o1.startSeconds() - o2.startSeconds())
            })
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ROUTINE -> {
                RoutineViewHolder(
                    ItemRoutineBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TaskViewHolder(
                    ItemTaskBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (occurrences[position].taskType) {
            null -> ROUTINE
            else -> TASK
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TaskViewHolder) {
            holder.bind(occurrences[position], taskClick)
        } else if (holder is RoutineViewHolder) {
            holder.bind(occurrences[position], routineClick)
        }
    }

    override fun getItemCount() = occurrences.size

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrence: com.naltynbekkz.timetable.model.Occurrence,
            taskClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
        ) {
            binding.task = occurrence
            binding.layout.setOnClickListener {
                taskClick(binding.task!!)
            }
        }

    }

    class RoutineViewHolder(private val binding: ItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            occurrence: com.naltynbekkz.timetable.model.Occurrence,
            routineClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
        ) {
            binding.routine = occurrence
            binding.layout.setOnClickListener {
                routineClick(binding.routine!!)
            }
        }
    }

}