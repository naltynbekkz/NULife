package com.naltynbekkz.nulife.ui.courses.courses.front

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCoursesBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.courses.adapter.UserCoursesAdapter
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.CoursesViewModel
import javax.inject.Inject


class CoursesFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: CoursesViewModel by viewModels { viewModelProvider.create(this) }

    private lateinit var binding: FragmentCoursesBinding
    private lateinit var coursesAdapter: UserCoursesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_courses, container, false)!!

        setHasOptionsMenu(true)

        (activity as MainActivity).setToolbar(binding.toolbar)

        viewModel.userCourses.observe(viewLifecycleOwner, Observer {
            coursesAdapter.submitList(it)
        })

        coursesAdapter = UserCoursesAdapter(
            click = fun(userCourse: UserCourse) {

                val a = NavOptions.Builder()
                    .setEnterAnim(R.anim.fade_in)
                    .setExitAnim(R.anim.fade_out)
                    .setPopEnterAnim(R.anim.fade_in)
                    .setPopExitAnim(R.anim.fade_out)
                    .build()

                findNavController().navigate(
                    CoursesFragmentDirections.actionCoursesFragmentToCourseFragment(
                        userCourse
                    ),
                    a
                )
            },
            longClick = fun(userCourse): Boolean {
                CourseDetailBottomSheet(
                    userCourse,
                    getCourse = viewModel::getCourse,
                    leave = fun(dismiss: () -> Unit) {
                        viewModel.unEnroll(
                            userCourse = userCourse,
                            failure = fun() {
                                Toast.makeText(
                                    context,
                                    "Something went wrong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            success = dismiss
                        )
                    },
                    edit = fun(userCourse) {
                        findNavController().navigate(
                            CoursesFragmentDirections.actionCoursesFragmentToEditCourseFragment(
                                userCourse
                            )
                        )
                    }
                ).show(parentFragmentManager, "TAG")
                return true
            }
        )
        binding.recyclerView.adapter = coursesAdapter

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enroll -> {
                findNavController().navigate(R.id.action_coursesFragment_to_enrollFragment)
            }
        }
        return true
    }


}
