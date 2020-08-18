package com.naltynbekkz.courses.ui.courses.front

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.core.Convert
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentEnrollCourseBinding
import com.naltynbekkz.courses.ui.courses.viewmodel.EnrollCourseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnrollCourseFragment : Fragment() {

    private val viewModel: EnrollCourseViewModel by viewModels()


    private lateinit var binding: FragmentEnrollCourseBinding

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                viewModel.userCourse.value?.let {
                    viewModel.enroll(
                        userCourse = it.apply {
                            color = Convert.getColor(binding.colorSlider.selectedColor)
                        },
                        success = {
                            findNavController().navigate(R.id.action_enrollCourseFragment_to_coursesFragment)
                        },
                        failure = {
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT)
                                .show()
                        }
                    )
                }
            }
        }
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrollCourseBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        viewModel.userCourse.observe(viewLifecycleOwner, Observer { userCourse ->
            binding.userCourse = userCourse
        })

        binding.section.apply {
            minValue = 1
            maxValue = 45
            wrapSelectorWheel = false
            setOnValueChangedListener { _, _, newVal ->
                viewModel.userCourse.value?.let { userCourse ->
                    userCourse.section = newVal.toLong()
                }
            }
        }

        return binding.root
    }

}