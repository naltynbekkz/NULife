package com.naltynbekkz.courses.ui.courses.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.core.Constants.Companion.USER_COURSE
import com.naltynbekkz.core.TwoButtonsBottomSheet
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentCourseBinding
import com.naltynbekkz.courses.ui.deadlines.front.DeadlinesFragment
import com.naltynbekkz.courses.ui.questions.front.QuestionsFragment
import com.naltynbekkz.courses.ui.resources.front.ResourcesFragment

class CourseFragment : Fragment() {

    private val args: CourseFragmentArgs by navArgs()

    lateinit var binding: FragmentCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentCourseBinding.inflate(inflater, container, false)

        binding.toolbar.title = args.userCourse.id

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }


        binding.viewpager.adapter = CoursePagerAdapter()
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
                        CourseFragmentDirections.actionCourseFragmentToNewQuestionFragment(
                            null,
                            args.userCourse
                        )
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
                    ).show(requireActivity().supportFragmentManager, "tag")
                }
            }
        }

        return binding.root
    }

    inner class CoursePagerAdapter :
        FragmentStateAdapter(this) {

        val bundle = Bundle().apply {
            putSerializable(USER_COURSE, args.userCourse)
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