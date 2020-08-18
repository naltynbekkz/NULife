package com.naltynbekkz.timetable.ui

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.core.Constants
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.TwoButtonsBottomSheet
import com.naltynbekkz.timetable.R
import com.naltynbekkz.timetable.databinding.FragmentTimetableBinding
import java.util.*

class TimetableFragment : Fragment() {

    private var weekView = true
    private val today = Convert.removeHours()
    private lateinit var weekAdapter: WeekPagerAdapter
    private lateinit var semesterAdapter: SemesterPagerAdapter
    private lateinit var weekMediator: TabLayoutMediator
    private lateinit var semesterMediator: TabLayoutMediator

    private lateinit var topMenu: Menu

    private lateinit var callback: ViewPager2.OnPageChangeCallback

    private lateinit var binding: FragmentTimetableBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.timetable_menu, menu)
        topMenu = menu
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                TwoButtonsBottomSheet(
                    firstText = "New routine",
                    first = fun() {
                        findNavController().navigate(
                            TimetableFragmentDirections.actionTimetableFragmentToNewRoutineFragment(null, null)
                        )
                    },
                    secondText = "New task",
                    second = fun() {
                        findNavController().navigate(
                            TimetableFragmentDirections.actionTimetableFragmentToNewTaskFragment(
                                null
                            )
                        )
                    }
                ).show(parentFragmentManager, "TimetableFragment")
                return true
            }
            R.id.calendar -> {
                if (weekView) showSemester() else showWeek(false)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSemester() {
        weekMediator.detach()
        binding.viewpager.adapter = semesterAdapter
        semesterMediator.attach()
        weekView = false
        binding.viewpager.unregisterOnPageChangeCallback(callback)
        binding.toolbar.subtitle = null

        Handler().postDelayed({
            binding.viewpager.setCurrentItem(Convert.getMonth() - Convert.getMonth() / 3 * 3, true)
        }, 100)
        topMenu.findItem(R.id.calendar).setTitle(R.string.week)
    }

    private fun showWeek(first: Boolean) {
        semesterMediator.detach()
        binding.viewpager.adapter = weekAdapter
        weekMediator.attach()
        weekView = true
        binding.viewpager.registerOnPageChangeCallback(callback)
        Handler().postDelayed({
            binding.viewpager.setCurrentItem(3, true)
        }, 100)
        if (!first) {
            topMenu.findItem(R.id.calendar).setTitle(R.string.semester)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTimetableBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        weekAdapter = WeekPagerAdapter()
        semesterAdapter = SemesterPagerAdapter()

        weekMediator = TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            val secs = (position - 3) * 24 * 60 * 60 + today.timeInMillis / 1000
            tab.text = resources.getStringArray(R.array.week)[Convert.getDayOfWeek(secs)]
        }
        semesterMediator = TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            val month = Convert.getMonth() / 3 * 3 + position
            tab.text = resources.getStringArray(R.array.year)[month]
        }

        callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val secs = (position - 3) * 24 * 60 * 60 + today.timeInMillis / 1000
                binding.toolbar.subtitle = Convert.timestampToDate(secs)
            }
        }

        binding.viewpager.offscreenPageLimit = 4

        showWeek(true)

        return binding.root
    }


    inner class WeekPagerAdapter : FragmentStateAdapter(this) {

        override fun getItemCount() = 7

        override fun createFragment(position: Int): Fragment {
            val fragment = WeekdayFragment()
            fragment.arguments = Bundle().apply {
                putLong(
                    Constants.TODAY,
                    today.timeInMillis / 1000 + (position - 3) * 24 * 60 * 60
                )
            }
            return fragment
        }
    }

    inner class SemesterPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = 6

        override fun createFragment(position: Int): Fragment {
            val fragment = MonthFragment()
            fragment.arguments = Bundle().apply {
                putLong(
                    Constants.MONTH,
                    Convert.removeDays().apply {
                        set(Calendar.MONTH, Convert.getMonth() / 3 * 3)
                        add(Calendar.MONTH, position)
                    }.timeInMillis / 1000
                )
            }
            return fragment
        }

    }

}
