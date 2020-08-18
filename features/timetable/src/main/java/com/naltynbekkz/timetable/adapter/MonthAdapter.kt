package com.naltynbekkz.timetable.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.databinding.ItemTimetableDayBinding

class MonthAdapter(
    private val taskClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit,
    private val routineClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var occurrences = ArrayList<MutableMap.MutableEntry<Long, ArrayList<com.naltynbekkz.timetable.model.Occurrence>>>()

    companion object {
        const val ITEM = 0
    }

    fun setOccurrences(occurrences: ArrayList<MutableMap.MutableEntry<Long, ArrayList<com.naltynbekkz.timetable.model.Occurrence>>>) {
        this.occurrences = occurrences
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return ItemViewHolder(
            ItemTimetableDayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int): Int {
        return ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(
                occurrences[position],
                taskClick,
                routineClick
            )
        }
    }

    override fun getItemCount() = occurrences.size

    class ItemViewHolder(private val binding: ItemTimetableDayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            day: Map.Entry<Long, ArrayList<com.naltynbekkz.timetable.model.Occurrence>>,
            taskClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit,
            routineClick: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
        ) {
            binding.date.text = Convert.timestampToDay(day.key)
            binding.week.text = Convert.timestampToWeek(day.key)
            binding.recyclerView.adapter =
                TimetableDayAdapter(day.value, taskClick, routineClick)
        }
    }

}