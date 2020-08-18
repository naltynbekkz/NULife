package com.naltynbekkz.timetable.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.adapter.TaskDaysAdapter
import com.naltynbekkz.timetable.databinding.BottomSheetRoutineBinding
import com.naltynbekkz.timetable.model.Occurrence

class RoutineBottomSheet(
    private val routine: Occurrence,
    val edit: (Occurrence) -> Unit,
    val delete: (Occurrence) -> Unit
) : BottomSheetDialogFragment() {

    // TODO: ViewModel and submit the list

    private lateinit var binding: BottomSheetRoutineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_routine, container, false)
        binding.routine = routine
        binding.recyclerView.adapter = TaskDaysAdapter()
        binding.arrow.setOnClickListener {
            dismiss()
        }
        binding.editRoutine.setOnClickListener {
            dismiss()
            edit(routine)
        }
        binding.deleteRoutine.setOnClickListener {
            dismiss()
            delete(routine)
        }
        return binding.root
    }
}