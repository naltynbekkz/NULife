package com.naltynbekkz.nulife.ui.clubs.front

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventsViewModel
import kotlinx.android.synthetic.main.activity_events.*
import javax.inject.Inject

class EventsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    val eventsViewModel: EventsViewModel by viewModels { viewModelProvider.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewpager.adapter = PagerAdapter(this)
        TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.events)[position]
        }.attach()

    }

    inner class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> EventsFragment(false)
                else -> EventsFragment(true)
            }
        }
    }

}