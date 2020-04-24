package com.naltynbekkz.nulife.ui.timetable.front

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.util.Constant
import com.naltynbekkz.nulife.util.Convert
import com.naltynbekkz.nulife.util.TwoButtonsBottomSheet
import kotlinx.android.synthetic.main.fragment_timetable.*
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbar(toolbar)

        weekAdapter = WeekPagerAdapter()
        semesterAdapter = SemesterPagerAdapter()

        weekMediator = TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            val secs = (position - 3) * 24 * 60 * 60 + today.timeInMillis / 1000
            tab.text = resources.getStringArray(R.array.week)[Convert.getDayOfWeek(secs)]
        }
        semesterMediator = TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            val month = Convert.getMonth() / 3 * 3 + position
            tab.text = resources.getStringArray(R.array.year)[month]
        }

        callback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                val secs = (position - 3) * 24 * 60 * 60 + today.timeInMillis / 1000
                toolbar.subtitle = Convert.timestampToDate(secs)
            }
        }

        viewpager.offscreenPageLimit = 4

        showWeek(true)

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
                        val intent = Intent(context, NewRoutineActivity::class.java)
                        startActivity(intent)
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
        viewpager.adapter = semesterAdapter
        semesterMediator.attach()
        weekView = false
        viewpager.unregisterOnPageChangeCallback(callback)
        toolbar.subtitle = null

        Handler().postDelayed({
            viewpager.setCurrentItem(Convert.getMonth() - Convert.getMonth() / 3 * 3, true)
        }, 100)
        topMenu.findItem(R.id.calendar).setTitle(R.string.week)
    }

    private fun showWeek(first: Boolean) {
        semesterMediator.detach()
        viewpager.adapter = weekAdapter
        weekMediator.attach()
        weekView = true
        viewpager.registerOnPageChangeCallback(callback)
        Handler().postDelayed({
            viewpager.setCurrentItem(3, true)
        }, 100)
        if (!first) {
            topMenu.findItem(R.id.calendar).setTitle(R.string.semester)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }


    inner class WeekPagerAdapter : FragmentStateAdapter(this) {

        override fun getItemCount() = 7

        override fun createFragment(position: Int): Fragment {
            val fragment = WeekdayFragment()
            fragment.arguments = Bundle().apply {
                putLong(
                    Constant.TODAY,
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
                    Constant.MONTH,
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
