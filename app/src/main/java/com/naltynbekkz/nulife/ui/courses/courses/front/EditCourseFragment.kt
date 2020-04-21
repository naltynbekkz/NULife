package com.naltynbekkz.nulife.ui.courses.courses.front

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentEditCourseBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EditCourseViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class EditCourseFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: EditCourseFragmentArgs by navArgs()
    private val viewModel: EditCourseViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }


    private lateinit var binding: FragmentEditCourseBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_course, container, false)
        setHasOptionsMenu(true)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
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
                        requireActivity().onBackPressed()
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
