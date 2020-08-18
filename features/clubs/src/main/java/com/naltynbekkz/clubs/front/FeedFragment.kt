package com.naltynbekkz.clubs.front

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.naltynbekkz.clubs.EventLayoutManager
import com.naltynbekkz.clubs.Sorting
import com.naltynbekkz.clubs.adapter.FancyEventsAdapter
import com.naltynbekkz.clubs.adapter.UserClubsAdapter
import com.naltynbekkz.clubs.databinding.FragmentFeedBinding
import com.naltynbekkz.clubs.viewmodel.EventsViewModel
import com.naltynbekkz.core.Convert
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FeedFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()
    private lateinit var fancyEventsAdapter: FancyEventsAdapter
    private lateinit var userClubsAdapter: UserClubsAdapter

    private lateinit var binding: FragmentFeedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFeedBinding.inflate(inflater, container, false)

        fancyEventsAdapter = FancyEventsAdapter(
            click = fun(eventId: String) {
                findNavController().navigate(
                    FeedFragmentDirections.actionFeedFragmentToEventFragment(eventId)
                )
            }
        )

        userClubsAdapter = UserClubsAdapter(
            click = fun(clubId: String) {
                findNavController().navigate(
                    FeedFragmentDirections.actionFeedFragmentToClubFragment(clubId)
                )
            }
        )

        viewModel.events.observe(viewLifecycleOwner, Observer {
            userClubsAdapter.submitList(it.second)
            fancyEventsAdapter.submitList(
                Sorting.sortSavedEvents(
                    it.first,
                    Sorting.sortMyEvents(it.second, it.third)
                )
            )
        })

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)


        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        LinearSnapHelper().attachToRecyclerView(binding.eventsRecyclerView)
        binding.eventsRecyclerView.layoutManager =
            EventLayoutManager(requireContext())
        binding.eventsRecyclerView.setPadding(
            width / 2 - Convert.dpToPx(210) / 2,
            0,
            width / 2 - Convert.dpToPx(210) / 2,
            0
        )

        binding.eventsRecyclerView.adapter = fancyEventsAdapter

        binding.myClubsRecyclerView.adapter = userClubsAdapter

        binding.allClubs.setOnClickListener {
            findNavController().navigate(
                FeedFragmentDirections.actionFeedFragmentToAllClubsFragment()
            )
        }
        binding.allEvents.setOnClickListener {
            findNavController().navigate(
                FeedFragmentDirections.actionFeedFragmentToAllEventsFragment()
            )
        }

        return binding.root

    }

}
