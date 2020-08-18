package com.naltynbekkz.timetable.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.timetable.adapter.WeekdayAdapter
import com.naltynbekkz.timetable.databinding.FragmentWeekdayBinding
import com.naltynbekkz.timetable.viewmodel.WeekdayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeekdayFragment : Fragment() {

    private val viewModel: WeekdayViewModel by viewModels()
    lateinit var adapter: WeekdayAdapter

    private lateinit var binding: FragmentWeekdayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = WeekdayAdapter(
            taskClick = ::showTaskBottomSheet,
            routineClick = ::showRoutineBottomSheet
        )
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekdayBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter

        viewModel.occurrences.observe(viewLifecycleOwner, Observer(adapter::setOccurrences))
        return binding.root
    }

    private fun showTaskBottomSheet(task: com.naltynbekkz.timetable.model.Occurrence) {
        TaskBottomSheet(
            task,
            fun(task: com.naltynbekkz.timetable.model.Occurrence) {
                findNavController().navigate(
                    TimetableFragmentDirections.actionTimetableFragmentToNewTaskFragment(task)
                )
            },
            viewModel::delete
        ).show(
            parentFragmentManager,
            "weekdayFragment"
        )
    }

    private fun showRoutineBottomSheet(routine: com.naltynbekkz.timetable.model.Occurrence) {
        RoutineBottomSheet(
            routine,
            fun(routine: com.naltynbekkz.timetable.model.Occurrence) {
                findNavController().navigate(
                    TimetableFragmentDirections.actionTimetableFragmentToNewRoutineFragment(
                        routine,
                        null
                    )
                )
            },
            viewModel::delete
        ).show(parentFragmentManager, "weekdayFragment")
    }
}