package com.naltynbekkz.timetable.ui

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.core.Convert
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.databinding.FragmentNewRoutineBinding
import com.naltynbekkz.timetable.model.Associate
import com.naltynbekkz.timetable.viewmodel.NewOccurrenceViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewRoutineFragment : Fragment() {
    val viewModel: NewOccurrenceViewModel by viewModels()

    lateinit var binding: FragmentNewRoutineBinding
    private lateinit var bottomSheet: AssociateBottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewRoutineBinding.inflate(inflater, container, false)
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
            TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, h, m ->
                    binding.routine =
                        binding.routine!!.apply { start = (h * 60 * 60 + m * 60).toLong() }
                },
                binding.routine!!.start.toInt() / 3600,
                binding.routine!!.start.toInt() / 60 % 60,
                false
            ).show()
        }

        binding.endText.setOnClickListener {
            TimePickerDialog(
                requireContext(), TimePickerDialog.OnTimeSetListener { _, h, m ->
                    binding.routine =
                        binding.routine!!.apply { end = (h * 60 * 60 + m * 60).toLong() }
                },
                binding.routine!!.end.toInt() / 3600,
                binding.routine!!.end.toInt() / 60 % 60,
                false
            ).show()
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.set_reminder)
                .setItems(
                    R.array.notify_string_array,
                    fun(_: DialogInterface, which: Int) {
                        binding.routine = binding.routine!!.apply {
                            notificationTime =
                                resources.getIntArray(R.array.notify_integer_array)[which].toLong()
                        }
                    })
                .show()
        }

        binding.routine = viewModel.routine

        if (viewModel.routine.isNew()) {
            initNew()
        } else {
            initEdit()
        }

    }

    private fun initNew() {

        binding.titleEditText.doOnTextChanged { _, _, _, _ ->
            if (binding.titleEditText.text.toString() != binding.associate?.title) {
                binding.associate = null
            }
        }

        bottomSheet = AssociateBottomSheet {
            binding.associate = it
            binding.titleEditText.setText(it.title)
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

        binding.associateTextView.setOnClickListener {
            bottomSheet.show(parentFragmentManager, "NewRoutineActivity")
        }
    }

    private fun initEdit() {

        binding.toolbar.title = "Edit routine"

        if (binding.routine!!.routineType != com.naltynbekkz.timetable.model.Occurrence.CUSTOM) {
            binding.associate = Associate(binding.routine!!)
        }

        binding.associate?.let {
            binding.associateEditText.text = it.title
        }


        val weekdays = listOf(
            binding.mon,
            binding.tue,
            binding.wed,
            binding.thu,
            binding.fri,
            binding.sat,
            binding.sun
        )

        for (i in weekdays.indices) {
            if (binding.routine!!.week!![i] == '1') {
                weekdays[i].isChecked = true
            }
        }

        val colors = resources.getIntArray(R.array.colors)
        for (i in colors.indices) {
            if (colors[i] == Color.parseColor(binding.routine!!.color)) {
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

                var week = ""

                listOf(
                    binding.mon,
                    binding.tue,
                    binding.wed,
                    binding.thu,
                    binding.fri,
                    binding.sat,
                    binding.sun
                ).forEach {
                    week += (if (it.isChecked) "1" else "0")
                }

                if (binding.routine!!.title.isNotEmpty() && week != "0000000") {
                    binding.loading = true

                    binding.routine!!.apply {
                        binding.associate?.type.let { routineType = it }
                        this.week = week
                        color = Convert.getColor(binding.colorSlider.selectedColor)

                    }

                    if (binding.routine!!.isNew()) {
                        viewModel.insert(binding.routine!!.apply {
                            id = binding.associate?.id ?: viewModel.getKey()
                        }, findNavController()::navigateUp)
                    } else {
                        viewModel.update(binding.routine!!, findNavController()::navigateUp)
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