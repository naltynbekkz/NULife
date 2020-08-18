package com.naltynbekkz.clubs.front

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.clubs.R
import com.naltynbekkz.clubs.databinding.FragmentEventBinding
import com.naltynbekkz.clubs.viewmodel.EventViewModel
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.model.Occurrence
import com.naltynbekkz.timetable.ui.AddTaskBottomSheet
import com.naltynbekkz.timetable.ui.TaskBottomSheet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : Fragment() {

    private val viewModel: EventViewModel by viewModels()

    lateinit var binding: FragmentEventBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
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
                    task = binding.event!!.toOccurrence(
                        Convert.getColor(
                            resources.getColor(
                                R.color.colorPrimary,
                                requireContext().theme
                            )
                        )
                    )
                ).show(parentFragmentManager, "EventActivity")
            } else {
                TaskBottomSheet(binding.task!!, fun(task: Occurrence) {
                    findNavController().navigate(
                        EventFragmentDirections.actionEventFragmentToNewTaskFragment(task)
                    )
                }, viewModel::delete).show(
                    parentFragmentManager,
                    "weekdayFragment"
                )
            }

        }
        return binding.root
    }
}