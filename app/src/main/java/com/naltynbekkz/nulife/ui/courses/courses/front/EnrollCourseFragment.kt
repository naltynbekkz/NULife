package com.naltynbekkz.nulife.ui.courses.courses.front

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentEnrollCourseBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EnrollCourseViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class EnrollCourseFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: EnrollCourseFragmentArgs by navArgs()
    private val viewModel: EnrollCourseViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }


    private lateinit var binding: FragmentEnrollCourseBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

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
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_enroll_course, container, false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        viewModel.userCourse.observe(viewLifecycleOwner, Observer { userCourse ->
            binding.userCourse = userCourse
            viewModel.setStudentCount()
        })

        binding.section.apply {
            minValue = 1
            maxValue = 45
            wrapSelectorWheel = false
            setOnValueChangedListener { _, _, newVal ->
                viewModel.userCourse.value?.let { userCourse ->
                    userCourse.section = newVal.toLong()
                    viewModel.setStudentCount()
                }
            }
        }

        return binding.root
    }

}