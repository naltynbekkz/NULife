package com.naltynbekkz.nulife.ui.food.front

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCafeBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Meal
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeViewModel
import com.naltynbekkz.nulife.util.Constant
import javax.inject.Inject

class CafeFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: CafeFragmentArgs by navArgs()
    val viewModel: CafeViewModel by viewModels { viewModelProvider.create(this, args.toBundle()) }

    private lateinit var binding: FragmentCafeBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).foodComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cafe, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        savedInstanceState?.getInt(Constant.COORDINATOR_LAYOUT_SCROLL_STATE)?.let {

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
            requireActivity().onBackPressed()
        }
        binding.viewpager.offscreenPageLimit = 2
        binding.viewpager.adapter = PagerAdapter(this, viewModel.cafeId)
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
            putString(Constant.CAFE_ID, cafeId)
        }

        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            val fragment = when (position) {
                0 -> CafeMenuFragment()
                else -> CafeDetailsFragment()
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constant.COORDINATOR_LAYOUT_SCROLL_STATE, binding.coordinatorLayout.scrollY)
    }

}
