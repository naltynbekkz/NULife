package com.naltynbekkz.nulife.ui.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemTimetableRoutineBinding
import com.naltynbekkz.nulife.databinding.ItemTimetableTaskBinding
import com.naltynbekkz.nulife.model.Occurrence

class TimetableDayAdapter(
    private val occurrences: ArrayList<Occurrence>,
    private val taskClick: (Occurrence) -> Unit,
    private val routineClick: (Occurrence) -> Unit
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
            occurrence: Occurrence,
            taskClick: (Occurrence) -> Unit
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
            occurrence: Occurrence,
            routineClick: (Occurrence) -> Unit
        ) {
            binding.routine = occurrence
            binding.layout.setOnClickListener {
                routineClick(binding.routine!!)
            }
        }
    }

}