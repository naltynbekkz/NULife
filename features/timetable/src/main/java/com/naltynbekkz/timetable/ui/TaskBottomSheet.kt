package com.naltynbekkz.timetable.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.timetable.databinding.BottomSheetTaskBinding

class TaskBottomSheet(
    private val task: com.naltynbekkz.timetable.model.Occurrence,
    private val edit: (com.naltynbekkz.timetable.model.Occurrence) -> Unit,
    private val delete: (com.naltynbekkz.timetable.model.Occurrence) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetTaskBinding.inflate(inflater, container, false)
        binding.task = task
        binding.arrow.setOnClickListener {
            dismiss()
        }
        binding.edit.setOnClickListener {
            dismiss()
            edit(task)
        }
        binding.delete.setOnClickListener {
            dismiss()
            delete(binding.task!!)
        }
        return binding.root
    }

}