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
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCafeMenuBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Meal
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.adapter.MealsAdapter
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeMenuViewModel
import javax.inject.Inject

class CafeMenuFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    val viewModel: CafeMenuViewModel by viewModels { viewModelProvider.create(this, arguments) }

    private lateinit var activeAdapter: MealsAdapter
    private lateinit var otherAdapter: MealsAdapter

    private lateinit var binding: FragmentCafeMenuBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).foodComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cafe_menu, container, false)

        activeAdapter = MealsAdapter(
            active = true,
            click = ::showMealDialog
        )
        otherAdapter = MealsAdapter(
            active = false,
            click = ::showMealDialog
        )

        viewModel.meals.observe(viewLifecycleOwner, Observer { meals ->
            val active = ArrayList<Meal>()
            val others = ArrayList<Meal>()
            meals.forEach {
                if (it.isAvailable()) {
                    active.add(it)
                } else {
                    others.add(it)
                }
            }
            activeAdapter.submitList(active)
            otherAdapter.submitList(others)
        })

        binding.recyclerView.adapter = activeAdapter
        binding.othersRecyclerView.adapter = otherAdapter

        return binding.root
    }

    // TODO: databinding
    private fun showMealDialog(meal: Meal?) {
        meal?.let {
            MealDialog(meal = it).show(parentFragmentManager, "tag")
        }
    }

}
