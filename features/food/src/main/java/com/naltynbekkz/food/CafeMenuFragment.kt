package com.naltynbekkz.food

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.naltynbekkz.food.adapter.MealsAdapter
import com.naltynbekkz.food.databinding.FragmentCafeMenuBinding
import com.naltynbekkz.food.model.Meal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CafeMenuFragment : Fragment() {

    val viewModel: CafeMenuViewModel by viewModels()

    private lateinit var activeAdapter: MealsAdapter
    private lateinit var otherAdapter: MealsAdapter

    private lateinit var binding: FragmentCafeMenuBinding

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
