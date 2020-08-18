package com.naltynbekkz.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.timetable.databinding.ItemTimetableRoutineBinding
import com.naltynbekkz.timetable.databinding.ItemTimetableTaskBinding

class TimetableDayAdapter(
    private val occurrences: ArrayList<com.naltynbekkz.timetable.model.Occurrence>,
    private val taskClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit,
    private val routineClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ROUTINE = 0
        const val TASK = 1
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return when (viewType) {
            ROUTINE -> {
                RoutineViewHolder(
                    ItemTimetableRoutineBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                TaskViewHolder(
                    ItemTimetableTaskBinding.inflate(
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
        when (holder) {
            is TaskViewHolder -> holder.bind(occurrences[position], taskClick)
            is RoutineViewHolder -> holder.bind(occurrences[position], routineClick)
        }
    }

    override fun getItemCount() = occurrences.size

    class TaskViewHolder(private val binding: ItemTimetableTaskBinding) :
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

    class RoutineViewHolder(private val binding: ItemTimetableRoutineBinding) :
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