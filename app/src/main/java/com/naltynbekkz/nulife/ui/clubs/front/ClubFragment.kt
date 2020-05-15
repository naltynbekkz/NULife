package com.naltynbekkz.nulife.ui.clubs.front

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
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.appbar.AppBarLayout
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentClubBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.clubs.adapter.ClubEventsAdapter
import com.naltynbekkz.nulife.ui.clubs.adapter.HeadsAdapter
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubViewModel
import com.naltynbekkz.nulife.util.Constants
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class ClubFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: ClubFragmentArgs by navArgs()
    private val viewModel: ClubViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    private lateinit var adapter: ClubEventsAdapter
    private lateinit var headsAdapter: HeadsAdapter

    lateinit var binding: FragmentClubBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).clubsComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_club, container, false)

        savedInstanceState?.let {
            binding.coordinatorLayout.scrollY = it.getInt(Constants.COORDINATOR_LAYOUT_SCROLL_STATE)
            binding.nestedScrollView.scrollY = it.getInt(Constants.NESTED_SCROLL_STATE)
        }

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.clubTitle.isSelected = true

        headsAdapter = HeadsAdapter()
        adapter = ClubEventsAdapter(
            click = fun(eventId: String) {
                findNavController().navigate(
                    ClubFragmentDirections.actionClubFragmentToEventFragment(eventId)
                )
            }
        )

        binding.eventsRecyclerView.adapter = adapter
        binding.headsRecyclerView.adapter = headsAdapter

        viewModel.club.observe(viewLifecycleOwner, Observer { club ->
            binding.club = club
            binding.loading = false
            binding.follow.setOnClickListener {
                if (club.following()) {
                    viewModel.unFollow()
                } else {
                    viewModel.follow()
                }
                binding.loading = true
            }
            headsAdapter.submitList(club.heads)
        })

        viewModel.events.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        binding.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val progress = -verticalOffset / appBarLayout.totalScrollRange.toFloat()
            binding.motionLayout.progress = progress
            binding.bodyMotionLayout.progress = progress
        })

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(
            Constants.COORDINATOR_LAYOUT_SCROLL_STATE,
            binding.coordinatorLayout.scrollY
        )
        outState.putInt(Constants.NESTED_SCROLL_STATE, binding.nestedScrollView.scrollY)
    }

}