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
import com.naltynbekkz.nulife.databinding.FragmentCafeDetailsBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.adapter.SmallReviewAdapter
import com.naltynbekkz.nulife.ui.food.viewmodel.CafeDetailsViewModel
import com.naltynbekkz.nulife.util.Constant
import javax.inject.Inject

class CafeDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: CafeDetailsViewModel by viewModels {
        viewModelProvider.create(this, arguments)
    }

    private lateinit var adapter: SmallReviewAdapter
    private lateinit var binding: FragmentCafeDetailsBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).foodComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cafe_details, container, false)

        savedInstanceState?.getInt(Constant.NESTED_SCROLL_STATE)?.let {
            binding.nestedScrollView.scrollY = it
        }


        adapter = SmallReviewAdapter {
            findNavController().navigate(
                CafeFragmentDirections.actionCafeFragmentToReviewsFragment(viewModel.cafeId)
            )
        }

        binding.recyclerView.adapter = adapter
        binding.ratingTextView.setOnClickListener {
            findNavController().navigate(
                CafeFragmentDirections.actionCafeFragmentToNewReviewFragment(viewModel.cafeId, 0L)
            )
        }
        binding.ratingBar.setOnRatingBarChangeListener { _, rating, fromUser ->
            viewModel.cafe.value?.let {
                if (rating != it.myRating() && rating != 0f && fromUser) {
                    findNavController().navigate(
                        CafeFragmentDirections.actionCafeFragmentToNewReviewFragment(
                            viewModel.cafeId,
                            rating.toLong()
                        )
                    )
                }
            }
        }

        viewModel.cafe.observe(viewLifecycleOwner, Observer { cafe ->
            binding.cafe = cafe
            adapter.setData(cafe.reviews)
        })

        return binding.root

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(Constant.NESTED_SCROLL_STATE, binding.nestedScrollView.scrollY)
    }

}
