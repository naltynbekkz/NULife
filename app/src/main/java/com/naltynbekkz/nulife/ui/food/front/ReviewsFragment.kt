package com.naltynbekkz.nulife.ui.food.front

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentReviewsBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.adapter.ReviewsAdapter
import com.naltynbekkz.nulife.ui.food.viewmodel.ReviewViewModel
import javax.inject.Inject

class ReviewsFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: ReviewsFragmentArgs by navArgs()
    val viewModel: ReviewViewModel by viewModels { viewModelProvider.create(this, args.toBundle()) }

    lateinit var adapter: ReviewsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).foodComponent.inject(this)
    }

    private lateinit var binding: FragmentReviewsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reviews, container, false)
        setHasOptionsMenu(true)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        adapter = ReviewsAdapter()

        viewModel.reviews.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.recyclerView.adapter = adapter
        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addReview -> {
                findNavController().navigate(
                    ReviewsFragmentDirections.actionReviewsFragmentToNewReviewFragment(viewModel.cafeId, 0L)
                )
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.reviews_menu, menu)
    }

}