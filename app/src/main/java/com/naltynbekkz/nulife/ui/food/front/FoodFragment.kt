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
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentFoodBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.adapter.CafeAdapter
import com.naltynbekkz.nulife.ui.food.viewmodel.CafesViewModel
import javax.inject.Inject

class FoodFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    val viewModel: CafesViewModel by viewModels { viewModelProvider.create(this) }

    private lateinit var binding: FragmentFoodBinding

    lateinit var adapter: CafeAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).foodComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CafeAdapter { cafeId ->
            findNavController().navigate(
                FoodFragmentDirections.actionFoodFragmentToCafeFragment(cafeId)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food, container, false)

        (activity as MainActivity).setToolbar(binding.toolbar)

        viewModel.cafes.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.recyclerView.adapter = adapter
        return binding.root
    }

}
