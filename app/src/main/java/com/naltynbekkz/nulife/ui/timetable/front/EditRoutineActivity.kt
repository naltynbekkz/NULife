package com.naltynbekkz.nulife.ui.timetable.front

import android.graphics.Color
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence

class EditRoutineActivity : NewRoutineActivity() {

    override fun init() {

        binding.toolbar.title = "Edit routine"

        binding.routine = intent.extras!!.get("routine") as Occurrence
        if (binding.routine!!.routineType != Occurrence.CUSTOM) {
            binding.associate = Associate(binding.routine!!)
        }

        binding.associate?.let {
            binding.associateEditText.text = it.title
        }


        val weekdays = listOf(
            binding.mon,
            binding.tue,
            binding.wed,
            binding.thu,
            binding.fri,
            binding.sat,
            binding.sun
        )

        for (i in weekdays.indices) {
            if (binding.routine!!.week!![i] == '1') {
                weekdays[i].isChecked = true
            }
        }

        val colors = resources.getIntArray(R.array.colors)
        for (i in colors.indices) {
            if (colors[i] == Color.parseColor(binding.routine!!.color)) {
                binding.colorSlider.setSelection(i)
            }
        }

    }

}