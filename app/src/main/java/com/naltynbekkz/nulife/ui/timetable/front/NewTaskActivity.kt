package com.naltynbekkz.nulife.ui.timetable.front

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityNewTaskBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.timetable.viewmodel.NewOccurrenceViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

open class NewTaskActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    lateinit var binding: ActivityNewTaskBinding
    val viewModel: NewOccurrenceViewModel by viewModels { viewModelProvider.create(this) }
    private lateinit var bottomSheet: AssociateBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_task)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
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
                    this,
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
            picker.show(supportFragmentManager, picker.toString())
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
                    this,
                    TimePickerDialog.OnTimeSetListener { _, h, m ->
                        binding.task = binding.task!!.apply {
                            end = Convert.setSeconds(binding.task!!.end, h, m)

                        }
                    },
                    0, 0,
                    false
                ).show()
            }
            picker.show(supportFragmentManager, picker.toString())
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(this)
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

        init()
    }


    open fun init() {

        binding.task = Occurrence(task = true)

        bottomSheet = AssociateBottomSheet(
            viewModel.userCourses.value,
            viewModel.userClubs.value,
            viewModel.routines.value
        ) {
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

        binding.associateTextView.setOnClickListener {
            bottomSheet.show(supportFragmentManager, "NewTaskActivity")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
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
                        viewModel.insertTask(
                            task = binding.task!!.apply {
                                id = FirebaseDatabase.getInstance().reference.push().key!!
                            },
                            complete = ::finish
                        )

                    } else {
                        viewModel.updateTask(
                            task = binding.task!!,
                            complete = ::finish
                        )
                    }

                } else {
                    binding.loading = false
                }
            }
            R.id.help -> {
                Convert.help(this)
            }
        }
        return true
    }

}