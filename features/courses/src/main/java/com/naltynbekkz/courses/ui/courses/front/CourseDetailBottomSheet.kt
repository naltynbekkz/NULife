package com.naltynbekkz.courses.ui.courses.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.courses.databinding.BottomSheetCourseDetailBinding
import com.naltynbekkz.courses.model.Course
import com.naltynbekkz.timetable.model.UserCourse
import com.naltynbekkz.courses.ui.courses.adapter.StudentsAdapter

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
        binding = BottomSheetCourseDetailBinding.inflate(inflater, container, false)
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