package com.naltynbekkz.nulife.ui.timetable.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetTaskBinding
import com.naltynbekkz.nulife.model.Occurrence

class TaskBottomSheet(
    private val task: Occurrence,
    private val edit: (Occurrence) -> Unit,
    private val delete: (Occurrence) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_task, container, false)
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