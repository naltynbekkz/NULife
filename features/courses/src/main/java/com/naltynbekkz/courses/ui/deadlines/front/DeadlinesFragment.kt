package com.naltynbekkz.courses.ui.deadlines.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.courses.databinding.FragmentDeadlinesBinding
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.courses.ui.courses.front.CourseFragmentDirections
import com.naltynbekkz.courses.ui.deadlines.adapter.DeadlineDaysAdapter
import com.naltynbekkz.courses.ui.deadlines.viewmodel.DeadlinesViewModel
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.ui.AddTaskBottomSheet
import com.naltynbekkz.timetable.ui.TaskBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeadlinesFragment : Fragment() {

    private val viewModel: DeadlinesViewModel by viewModels()

    private lateinit var adapter: DeadlineDaysAdapter

    private fun showDeadlineBottomSheet(
        task: Occurrence?,
        deadline: Deadline
    ) {
        if (task != null) {
            TaskBottomSheet(
                task = task,
                edit = fun(task: Occurrence) {
                    findNavController().navigate(
                        CourseFragmentDirections.actionCourseFragmentToNewTaskFragment(task)
                    )
                },
                delete = viewModel::delete
            ).show(parentFragmentManager, "tag")
        } else {
            AddTaskBottomSheet(
                task = deadline.toOccurrence(viewModel.userCourse),
                insert = viewModel::insert
            ).show(parentFragmentManager, "tag")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDeadlinesBinding.inflate(inflater, container, false)

        adapter = DeadlineDaysAdapter(
            click = ::showDeadlineBottomSheet
        )

        viewModel.deadlines.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            adapter.setSavedData(it)
        })

        binding.recyclerView.adapter = adapter

        return binding.root
    }

}
