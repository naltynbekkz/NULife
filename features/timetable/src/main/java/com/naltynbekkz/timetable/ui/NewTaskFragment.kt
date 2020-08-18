package com.naltynbekkz.timetable.ui

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.databinding.FragmentNewTaskBinding
import com.naltynbekkz.timetable.model.Associate
import com.naltynbekkz.timetable.viewmodel.NewOccurrenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewTaskFragment : Fragment() {
    val viewModel: NewOccurrenceViewModel by viewModels()

    lateinit var binding: FragmentNewTaskBinding
    private lateinit var bottomSheet: AssociateBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.startText.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(binding.task!!.start * 1000)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                .setTitleText(R.string.select_date).build()
            picker.addOnPositiveButtonClickListener {
                binding.task = binding.task!!.apply {
                    start =
                        Convert.removeHours((it - Convert.getZoneOffset()) / 1000).timeInMillis / 1000
                }
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, h, m ->
                        binding.task = binding.task!!.apply {
                            start = Convert.setSeconds(binding.task!!.start, h, m)
                        }
                    },
                    0,
                    0,
                    false
                ).show()
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        binding.endText.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(if (binding.task!!.end != 0L) binding.task!!.end * 1000 else System.currentTimeMillis())
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                .setTitleText(R.string.select_date).build()
            picker.addOnPositiveButtonClickListener {
                binding.task = binding.task!!.apply {
                    end = (it - Convert.getZoneOffset()) / 1000
                }
                val a = Convert.getSeconds(binding.task!!.end)
                TimePickerDialog(
                    requireContext(),
                    TimePickerDialog.OnTimeSetListener { _, h, m ->
                        binding.task = binding.task!!.apply {
                            end = Convert.setSeconds(binding.task!!.end, h, m)

                        }
                    },
                    0, 0,
                    false
                ).show()
            }
            picker.show(parentFragmentManager, picker.toString())
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Title")
                .setItems(
                    R.array.notify_string_array,
                    fun(_: DialogInterface, which: Int) {
                        binding.task = binding.task!!.apply {
                            notificationTime =
                                resources.getIntArray(R.array.notify_integer_array)[which].toLong()
                        }
                    })
                .show()
        }
        binding.task = viewModel.task

        if (viewModel.task.isNew()) {
            initNew()
        } else {
            initEdit()
        }

    }

    private fun initNew() {
        bottomSheet = AssociateBottomSheet {
            binding.associate = it
            if (it.color != null) {
                val colors = resources.getIntArray(R.array.colors)
                for (i in colors.indices) {
                    if (colors[i] == Color.parseColor(it.color)) {
                        binding.colorSlider.setSelection(i)
                    }
                }
            }
        }

        viewModel.userCourses.observe(viewLifecycleOwner, Observer {
            bottomSheet.userCourses = it
        })
        viewModel.userClubs.observe(viewLifecycleOwner, Observer {
            bottomSheet.userClubs = it
        })
        viewModel.routines.observe(viewLifecycleOwner, Observer {
            bottomSheet.routines = it
        })

        binding.associateTextView.setOnClickListener {
            bottomSheet.show(parentFragmentManager, "NewTaskActivity")
        }
    }

    private fun initEdit() {
        binding.toolbar.title = "Edit task"

        if (binding.task!!.parentType != null) {
            binding.associate = Associate(binding.task!!)
        }

        val colors = resources.getIntArray(R.array.colors)
        for (i in colors.indices) {
            if (colors[i] == Color.parseColor(binding.task!!.color)) {
                binding.colorSlider.setSelection(i)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {

                if (binding.task!!.title.isNotEmpty() && binding.task!!.start != 0L) {
                    binding.loading = true

                    binding.task!!.apply {
                        binding.associate?.let {
                            parentType = it.type
                            parentTitle = it.title
                            parentId = it.id
                        }
                        color = Convert.getColor(binding.colorSlider.selectedColor)
                    }

                    if (binding.task!!.isNew()) {
                        viewModel.insert(
                            occurrence = binding.task!!.apply {
                                id = FirebaseDatabase.getInstance().reference.push().key!!
                            },
                            complete = findNavController()::navigateUp
                        )

                    } else {
                        viewModel.update(
                            occurrence = binding.task!!,
                            complete = findNavController()::navigateUp
                        )
                    }

                } else {
                    binding.loading = false
                }
            }
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }

}