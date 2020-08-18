package com.naltynbekkz.timetable.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.timetable.adapter.MonthAdapter
import com.naltynbekkz.timetable.databinding.FragmentMonthBinding
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.viewmodel.MonthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MonthFragment : Fragment() {
    private val viewModel: MonthViewModel by viewModels()

    lateinit var adapter: MonthAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = MonthAdapter(
            taskClick = ::showTaskBottomSheet,
            routineClick = ::showRoutineBottomSheet
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMonthBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter

        viewModel.occurrences.observe(viewLifecycleOwner, Observer {
            adapter.setOccurrences(it)
        })
        return binding.root
    }

    private fun showTaskBottomSheet(task: Occurrence) {
        TaskBottomSheet(
            task,
            fun(task: Occurrence) {
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

    private fun showRoutineBottomSheet(routine: Occurrence) {
        RoutineBottomSheet(
            routine,
            fun(routine: Occurrence) {
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