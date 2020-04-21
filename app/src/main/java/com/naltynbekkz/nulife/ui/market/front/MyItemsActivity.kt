package com.naltynbekkz.nulife.ui.market.front

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.ui.market.viewmodel.MyItemsViewModel
import kotlinx.android.synthetic.main.activity_items.*
import javax.inject.Inject

class MyItemsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    val viewModel: MyItemsViewModel by viewModels { viewModelProvider }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewpager.adapter = PagerAdapter(this)
        TabLayoutMediator(tab_layout, viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.items)[position]
        }.attach()

    }

    inner class PagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ItemsFragment(false)
                else -> ItemsFragment(true)
            }
        }
    }

}