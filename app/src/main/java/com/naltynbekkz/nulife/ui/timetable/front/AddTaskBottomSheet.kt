package com.naltynbekkz.nulife.ui.timetable.front

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetAddTaskBinding
import com.naltynbekkz.nulife.model.Occurrence

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_task, container, false)
        binding.task = task
        binding.arrow.setOnClickListener {
            dismiss()
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(context)
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