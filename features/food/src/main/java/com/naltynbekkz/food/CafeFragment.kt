package com.naltynbekkz.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.core.Constants
import com.naltynbekkz.food.databinding.FragmentCafeBinding
import com.naltynbekkz.food.model.Meal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CafeFragment : Fragment() {

    val viewModel: CafeViewModel by viewModels()

    private lateinit var binding: FragmentCafeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cafe, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        savedInstanceState?.getInt(Constants.COORDINATOR_LAYOUT_SCROLL_STATE)?.let {

            binding.coordinatorLayout.scrollY = it
        }

        viewModel.cafe.observe(viewLifecycleOwner, Observer { cafe ->
            binding.cafe = cafe
            cafe.featured?.let {
                // TODO looks not good
                viewModel.getFeatured(it).observe(viewLifecycleOwner, Observer { meal ->
                    binding.featured = meal
                })
            }
        })

        binding.image.setOnClickListener {
            showMealDialog(binding.featured)
        }

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            binding.motionLayout.progress =
                -verticalOffset / appBarLayout.totalScrollRange.toFloat()
        })

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.viewpager.offscreenPageLimit = 2
        binding.viewpager.adapter =
            PagerAdapter(
                this,
                viewModel.cafeId
            )
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { tab, position ->
            tab.text = resources.getStringArray(R.array.cafe)[position]
        }.attach()

    }

    // TODO databinding
    fun showMealDialog(meal: Meal?) {
        meal?.let {
            MealDialog(it).show(parentFragmentManager, "tag")
        }
    }

    class PagerAdapter(fragment: Fragment, cafeId: String) : FragmentStateAdapter(fragment) {

        val bundle = Bundle().apply {
            putString(Constants.CAFE_ID, cafeId)
        }

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            val fragment = when (position) {
                0 -> CafeMenuFragment()
                else -> com.naltynbekkz.food.CafeDetailsFragment()
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            Constants.COORDINATOR_LAYOUT_SCROLL_STATE,
            binding.coordinatorLayout.scrollY
        )
    }

}
