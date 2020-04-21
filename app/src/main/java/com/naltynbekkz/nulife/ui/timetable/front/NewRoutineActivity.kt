package com.naltynbekkz.nulife.ui.timetable.front

import android.app.TimePickerDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityNewRoutineBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Associate
import com.naltynbekkz.nulife.model.Occurrence
import com.naltynbekkz.nulife.ui.timetable.viewmodel.NewOccurrenceViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

open class NewRoutineActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    lateinit var binding: ActivityNewRoutineBinding
    val viewModel: NewOccurrenceViewModel by viewModels { viewModelProvider.create(this) }
    private lateinit var bottomSheet: AssociateBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_routine)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.startText.setOnClickListener {
            TimePickerDialog(
                this,
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
                this, TimePickerDialog.OnTimeSetListener { _, h, m ->
                    binding.routine =
                        binding.routine!!.apply { end = (h * 60 * 60 + m * 60).toLong() }
                },
                binding.routine!!.end.toInt() / 3600,
                binding.routine!!.end.toInt() / 60 % 60,
                false
            ).show()
        }

        binding.notify.setOnClickListener {
            MaterialAlertDialogBuilder(this)
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

        init()

    }

    open fun init() {

        binding.routine = Occurrence(intent.extras?.get("associate") as Associate?)

        binding.titleEditText.doOnTextChanged { _, _, _, _ ->
            if (binding.titleEditText.text.toString() != binding.associate?.title) {
                binding.associate = null
            }
        }

        bottomSheet = AssociateBottomSheet(
            viewModel.userCourses.value,
            viewModel.userClubs.value
        ) {
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

        binding.associateTextView.setOnClickListener {
            bottomSheet.show(supportFragmentManager, "NewRoutineActivity")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
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
                        }, ::finish)
                    } else {
                        viewModel.updateRoutine(binding.routine!!, ::finish)
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