package com.naltynbekkz.nulife.ui.timetable.front

import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentNewRoutineBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.timetable.viewmodel.NewOccurrenceViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class NewRoutineFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    val args: NewRoutineFragmentArgs by navArgs()
    val viewModel: NewOccurrenceViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    lateinit var binding: FragmentNewRoutineBinding
    private lateinit var bottomSheet: AssociateBottomSheet

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).timetableComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_routine, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
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

        if (binding.routine!!.routineType != Occurrence.CUSTOM) {
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
                        viewModel.insertRoutine(binding.routine!!.apply {
                            id = binding.associate?.id
                                ?: FirebaseDatabase.getInstance().reference.push().key!!
                        }, requireActivity()::finish)
                    } else {
                        viewModel.updateRoutine(binding.routine!!, requireActivity()::finish)
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