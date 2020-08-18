package com.naltynbekkz.courses.ui.courses.front

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.courses.ui.courses.viewmodel.EditCourseViewModel
import com.naltynbekkz.core.Convert
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentEditCourseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditCourseFragment : Fragment() {

    private val viewModel: EditCourseViewModel by viewModels()


    private lateinit var binding: FragmentEditCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_course, container, false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.userCourse = viewModel.userCourse

        val colors = resources.getIntArray(R.array.colors)
        for (i in colors.indices) {
            if (colors[i] == Color.parseColor(viewModel.userCourse.color)) {
                binding.colorSlider.setSelection(i)
            }
        }

        viewModel.professor.observe(viewLifecycleOwner, Observer {
            binding.professor = it
        })

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                viewModel.edit(
                    success = {
                        findNavController().navigateUp()
                    },
                    failure = {
                        Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT)
                            .show()
                    },
                    professor = binding.professor ?: "",
                    color = Convert.getColor(binding.colorSlider.selectedColor)
                )
            }
        }
        return true
    }

}
