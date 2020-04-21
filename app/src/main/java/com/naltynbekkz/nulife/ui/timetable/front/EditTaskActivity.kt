package com.naltynbekkz.nulife.ui.timetable.front

import android.graphics.Color
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import kotlinx.android.synthetic.main.activity_new_task.*

class EditTaskActivity : NewTaskActivity() {

    override fun init() {
        toolbar.title = "Edit task"
        binding.task = intent.extras!!.get("task") as Occurrence

        if (binding.task!!.parentType != null) {
            binding.associate = Associate(binding.task!!)
        }

        val colors = resources.getIntArray(R.array.colors)
        for (i in colors.indices) {
            if (colors[i] == Color.parseColor(binding.task!!.color)) {
                colorSlider.setSelection(i)
            }
        }

    }
}