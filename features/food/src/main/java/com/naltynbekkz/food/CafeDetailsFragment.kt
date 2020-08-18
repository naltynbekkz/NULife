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
import com.naltynbekkz.core.Constants
import com.naltynbekkz.food.adapter.SmallReviewAdapter
import com.naltynbekkz.food.databinding.FragmentCafeDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CafeDetailsFragment : Fragment() {

    private val viewModel: CafeDetailsViewModel by viewModels()

    private lateinit var adapter: SmallReviewAdapter
    private lateinit var binding: FragmentCafeDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_cafe_details, container, false)

        savedInstanceState?.getInt(Constants.NESTED_SCROLL_STATE)?.let {
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
        outState.putInt(Constants.NESTED_SCROLL_STATE, binding.nestedScrollView.scrollY)
    }

}
