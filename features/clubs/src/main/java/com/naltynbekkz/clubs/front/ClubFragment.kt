package com.naltynbekkz.clubs.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.naltynbekkz.clubs.adapter.ClubEventsAdapter
import com.naltynbekkz.clubs.adapter.HeadsAdapter
import com.naltynbekkz.clubs.databinding.FragmentClubBinding
import com.naltynbekkz.clubs.viewmodel.ClubViewModel
import com.naltynbekkz.core.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubFragment : Fragment() {

    private val viewModel: ClubViewModel by viewModels()

    private lateinit var adapter: ClubEventsAdapter
    private lateinit var headsAdapter: HeadsAdapter

    lateinit var binding: FragmentClubBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentClubBinding.inflate(inflater, container, false)

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