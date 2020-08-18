package com.naltynbekkz.courses.ui.courses.front

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.core.BottomNavigationController
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentCoursesBinding
import com.naltynbekkz.courses.ui.courses.adapter.UserCoursesAdapter
import com.naltynbekkz.courses.ui.courses.viewmodel.CoursesViewModel
import com.naltynbekkz.timetable.model.UserCourse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoursesFragment : Fragment() {

    private val viewModel: CoursesViewModel by viewModels()

    private lateinit var binding: FragmentCoursesBinding
    private lateinit var coursesAdapter: UserCoursesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCoursesBinding.inflate(inflater, container, false)

        (activity as BottomNavigationController.HidingBottomNav).showBottomNav()

        setHasOptionsMenu(true)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        viewModel.userCourses.observe(viewLifecycleOwner, Observer {
            coursesAdapter.submitList(it)
        })

        coursesAdapter = UserCoursesAdapter(
            click = fun(userCourse: UserCourse) {

                findNavController().navigate(
                    CoursesFragmentDirections.actionCoursesFragmentToCourseFragment(
                        userCourse
                    )
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
        inflater.inflate(R.menu.courses_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.enroll -> {

                findNavController().navigate(
                    CoursesFragmentDirections.actionCoursesFragmentToEnrollFragment(),
                    NavOptions.Builder()
                        .setEnterAnim(R.anim.fade_in)
                        .setExitAnim(R.anim.fade_out)
                        .setPopEnterAnim(R.anim.fade_in)
                        .setPopExitAnim(R.anim.fade_out)
                        .build()
                )
            }
        }
        return true
    }


}