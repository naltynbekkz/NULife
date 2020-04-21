package com.naltynbekkz.nulife.ui.clubs.front

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentEventBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventViewModel
import com.naltynbekkz.nulife.ui.timetable.front.AddTaskBottomSheet
import com.naltynbekkz.nulife.ui.timetable.front.EditTaskActivity
import com.naltynbekkz.nulife.ui.timetable.front.TaskBottomSheet
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class EventFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: EventViewModel by viewModels { viewModelProvider.create(this, args.toBundle()) }
    private val args: EventFragmentArgs by navArgs()

    lateinit var binding: FragmentEventBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).clubsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_event, container, false)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        viewModel.event.observe(viewLifecycleOwner, Observer {
            binding.event = it
        })
        viewModel.task.observe(viewLifecycleOwner, Observer {
            binding.task = it
        })

        listOf(binding.logo, binding.clubTitle).forEach {
            it.setOnClickListener {
                binding.event?.let { event ->
                    findNavController().navigate(
                        EventFragmentDirections.actionEventFragmentToClubFragment(event.club.id)
                    )
                }
            }
        }

        binding.share.setOnClickListener {
            binding.event?.let { event ->
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, event.getShare())
                    type = "text/plain"
                }
                startActivity(Intent.createChooser(intent, "Share"))
            }
        }

        binding.title.isSelected = true

        binding.link.setOnClickListener {
            binding.event?.let { event ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(event.registration?.link)
                startActivity(intent)
            }
        }

        binding.addToTimetable.setOnClickListener {

            if (binding.task == null) {
                AddTaskBottomSheet(
                    insert = viewModel::insert,
                    task = Occurrence(
                        event = binding.event!!,
                        color = Convert.getColor(
                            resources.getColor(
                                R.color.colorPrimary,
                                requireContext().theme
                            )
                        )
                    )
                ).show(parentFragmentManager, "EventActivity")
            } else {
                TaskBottomSheet(binding.task!!, fun(task: Occurrence) {
                    val intent = Intent(requireContext(), EditTaskActivity::class.java)
                    intent.putExtra("task", task)
                    startActivity(intent)
                }, viewModel::delete).show(
                    parentFragmentManager,
                    "weekdayFragment"
                )
            }

        }
        return binding.root
    }
}