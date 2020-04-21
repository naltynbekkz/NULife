package com.naltynbekkz.nulife.ui.courses.deadlines.front

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Deadline
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.deadlines.adapter.DeadlineDaysAdapter
import com.naltynbekkz.nulife.ui.courses.deadlines.viewmodel.DeadlinesViewModel
import com.naltynbekkz.nulife.ui.timetable.front.AddTaskBottomSheet
import com.naltynbekkz.nulife.ui.timetable.front.EditTaskActivity
import com.naltynbekkz.nulife.ui.timetable.front.TaskBottomSheet
import kotlinx.android.synthetic.main.fragment_deadlines.*
import javax.inject.Inject

class DeadlinesFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: DeadlinesViewModel by viewModels {
        viewModelProvider.create(this, arguments)
    }

    private lateinit var adapter: DeadlineDaysAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    private fun showDeadlineBottomSheet(
        task: Occurrence?,
        deadline: Deadline
    ) {
        if (task != null) {
            TaskBottomSheet(
                task = task,
                edit = fun(task: Occurrence) {
                    // TODO: add to activitybuildersmodule
                    val intent = Intent(context, EditTaskActivity::class.java)
                    intent.putExtra("task", task)
                    startActivity(intent)
                },
                delete = viewModel::delete
            ).show(parentFragmentManager, "tag")
        } else {
            AddTaskBottomSheet(
                task = Occurrence(deadline, viewModel.userCourse),
                insert = viewModel::insert
            ).show(parentFragmentManager, "tag")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_deadlines, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DeadlineDaysAdapter(
            click = ::showDeadlineBottomSheet
        )

        viewModel.deadlines.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            adapter.setSavedData(it)
        })

        recycler_view.adapter = adapter

    }


}
