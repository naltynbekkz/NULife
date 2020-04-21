package com.naltynbekkz.nulife.ui.courses.courses.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCourseBinding
import com.naltynbekkz.nulife.model.UserCourse
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.deadlines.front.DeadlinesFragment
import com.naltynbekkz.nulife.ui.courses.questions.front.QuestionsFragment
import com.naltynbekkz.nulife.ui.courses.resources.front.ResourcesFragment
import com.naltynbekkz.nulife.util.Constant.Companion.USER_COURSE
import com.naltynbekkz.nulife.util.TwoButtonsBottomSheet

class CourseFragment : Fragment() {

    private val args: CourseFragmentArgs by navArgs()

    lateinit var binding: FragmentCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_course, container, false)

        binding.toolbar.title = args.userCourse.id

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }


        binding.viewpager.adapter = CoursePagerAdapter(args.userCourse)
        binding.viewpager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.course)[position]
        }.attach()


        binding.viewpager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.fab.apply {
                    animate()
                        .rotationBy(45f)
                        .setDuration(100)
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .withEndAction {
                            setImageResource(
                                when (position) {
                                    0 -> R.drawable.ic_add_circle
                                    1 -> R.drawable.ic_new_deadline
                                    else -> R.drawable.ic_new_resourse
                                }
                            )
                            rotation = -45f
                            scaleX = 1.1f
                            scaleY = 1.1f
                            animate()
                                .rotationBy(45f)
                                .setDuration(100)
                                .scaleX(1f)
                                .scaleY(1f)
                                .start()
                        }
                        .start()
                }
            }
        })

        binding.fab.setOnClickListener {
            when (binding.viewpager.currentItem) {
                0 -> {
                    findNavController().navigate(
                        CourseFragmentDirections.actionCourseFragmentToNewQuestionFragment(args.userCourse)
                    )
                }
                1 -> {
                    findNavController().navigate(
                        CourseFragmentDirections.actionCourseFragmentToNewDeadlineFragment(args.userCourse)
                    )
                }
                else -> {
                    TwoButtonsBottomSheet(
                        first = fun() {
                            findNavController().navigate(
                                CourseFragmentDirections.actionCourseFragmentToNewResourceFragment(
                                    args.userCourse
                                )
                            )
                        },
                        firstText = "File",
                        secondText = "Images",
                        second = fun() {
                            findNavController().navigate(
                                CourseFragmentDirections.actionCourseFragmentToNewImagesFragment(
                                    args.userCourse
                                )
                            )
                        }
                    ).show(activity!!.supportFragmentManager, "tag")
                }
            }
        }

        return binding.root
    }

    inner class CoursePagerAdapter(userCourse: UserCourse) :
        FragmentStateAdapter(this) {

        val bundle = Bundle().apply {
            putSerializable(USER_COURSE, userCourse)
        }

        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment {

            val fragment = when (position) {
                0 -> QuestionsFragment()
                1 -> DeadlinesFragment()
                2 -> ResourcesFragment()
                else -> throw RuntimeException("only 3 fragments")
            }

            fragment.arguments = bundle
            return fragment
        }
    }

}