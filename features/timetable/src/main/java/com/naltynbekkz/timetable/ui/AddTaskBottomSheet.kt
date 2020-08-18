package com.naltynbekkz.timetable.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.databinding.BottomSheetAddTaskBinding
import com.naltynbekkz.timetable.model.Occurrence


class AddTaskBottomSheet(
    val insert: (Occurrence) -> Unit,
    val task: Occurrence
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetAddTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetAddTaskBinding.inflate(inflater, container, false)
        binding.task = task
        binding.arrow.setOnClickListener {
            dismiss()
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.set_reminder)
                .setItems(
                    R.array.notify_string_array,
                    fun(_: DialogInterface, which: Int) {
                        binding.task = binding.task!!.apply {
                            notificationTime =
                                resources.getIntArray(R.array.notify_integer_array)[which].toLong()
                        }
                    })
                .show()
        }

        binding.add.setOnClickListener {
            insert(binding.task!!)
            dismiss()
        }

        return binding.root
    }

}