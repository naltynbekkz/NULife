package com.naltynbekkz.courses.ui.deadlines.front

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.naltynbekkz.core.Convert
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentNewDeadlineBinding
import com.naltynbekkz.courses.model.Deadline
import com.naltynbekkz.courses.ui.deadlines.viewmodel.DeadlinesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewDeadlineFragment : Fragment() {
    private val viewModel: DeadlinesViewModel by viewModels()

    lateinit var binding: FragmentNewDeadlineBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewDeadlineBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.deadline = Deadline(viewModel.userCourse)


        binding.setTimeTextView.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setSelection(binding.deadline!!.timestamp * 1000)
                .setCalendarConstraints(
                    CalendarConstraints.Builder()
                        .setValidator(DateValidatorPointForward.now()).build()
                )
                .setTitleText(R.string.select_date).build()
            picker.addOnPositiveButtonClickListener {
                binding.deadline = binding.deadline!!.apply {
                    timestamp =
                        (it - Convert.getZoneOffset()) / 1000 + Convert.getSeconds(
                            binding.deadline!!.timestamp
                        )
                }
                TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { _, h, m ->
                    binding.deadline = binding.deadline!!.apply {
                        timestamp = Convert.setSeconds(binding.deadline!!.timestamp, h, m)
                    }
                }, 0, 0, false).show()
            }
            picker.show(parentFragmentManager, picker.toString())
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.deadline!!.isValid()) {
                    binding.loading = true
                    viewModel.post(
                        deadline = binding.deadline!!,
                        allSections = binding.allSectionsSwitch.isChecked,
                        success = findNavController()::navigateUp,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
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
