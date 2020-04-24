package com.naltynbekkz.nulife.ui.timetable.front

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.timetable.adapter.MonthAdapter
import com.naltynbekkz.nulife.ui.timetable.viewmodel.MonthViewModel
import kotlinx.android.synthetic.main.fragment_month.*
import javax.inject.Inject

class MonthFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: MonthViewModel by viewModels {
        viewModelProvider.create(this, arguments)
    }

    lateinit var adapter: MonthAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).timetableComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = MonthAdapter(
            taskClick = ::showTaskBottomSheet,
            routineClick = ::showRoutineBottomSheet
        )

        viewModel.occurrences.observe(this, Observer {
            adapter.setOccurrences(it)
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_month, container, false)
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
                val intent = Intent(context, EditRoutineActivity::class.java)
                intent.putExtra("routine", routine)
                startActivity(intent)
            },
            viewModel::delete
        ).show(parentFragmentManager, "weekdayFragment")
    }
}