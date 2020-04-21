package com.naltynbekkz.nulife.ui.courses.courses.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.BottomSheetCourseDetailBinding
import com.naltynbekkz.nulife.model.Course
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.ui.courses.courses.adapter.StudentsAdapter

class CourseDetailBottomSheet(
    private val userCourse: UserCourse,
    val getCourse: (UserCourse) -> LiveData<Course>,
    val leave: (() -> Unit) -> Unit,
    val edit: (UserCourse) -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetCourseDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_course_detail, container, false)
        binding.userCourse = userCourse
        binding.arrow.setOnClickListener {
            dismiss()
        }
        binding.editCourse.setOnClickListener {
            if (binding.course != null) {
                dismiss()
                edit(binding.userCourse!!)
            }
        }
        binding.leave.setOnClickListener {
            leave(::dismiss)
        }

        getCourse(userCourse).observe(viewLifecycleOwner, Observer { course ->
            if (course != null) {
                binding.course = course
                binding.studentsRecyclerView.adapter = StudentsAdapter(course.students)
            }
        })

        return binding.root
    }
}