package com.naltynbekkz.clubs.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.clubs.R
import com.naltynbekkz.clubs.databinding.FragmentAllClubsBinding
import com.naltynbekkz.core.Constants

class AllClubsFragment : Fragment() {

    private lateinit var binding: FragmentAllClubsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAllClubsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.viewpager.adapter = PagerAdapter()
        binding.viewpager.offscreenPageLimit = 2
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.clubs)[position]
        }.attach()

    }


    inner class PagerAdapter : FragmentStateAdapter(this) {

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> ClubsFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(Constants.ALL, false)
                    }
                }
                1 -> ClubsFragment().apply {
                    arguments = Bundle().apply {
                        putBoolean(Constants.ALL, true)
                    }
                }
                else -> throw RuntimeException("too many fragments")
            }
        }
    }
}