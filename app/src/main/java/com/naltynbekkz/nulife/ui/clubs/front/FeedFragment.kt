package com.naltynbekkz.nulife.ui.clubs.front

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearSnapHelper
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentFeedBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.clubs.EventLayoutManager
import com.naltynbekkz.nulife.ui.clubs.adapter.FancyEventsAdapter
import com.naltynbekkz.nulife.ui.clubs.adapter.UserClubsAdapter
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventsViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class FeedFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: EventsViewModel by viewModels { viewModelProvider.create(this) }
    private lateinit var fancyEventsAdapter: FancyEventsAdapter
    private lateinit var userClubsAdapter: UserClubsAdapter

    private lateinit var binding: FragmentFeedBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).clubsComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feed, container, false)

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
        viewModel.userClubs.observe(viewLifecycleOwner, Observer {
            userClubsAdapter.submitList(it)
            fancyEventsAdapter.submitList(
                Convert.sortSavedEvents(
                    viewModel.tasks.value,
                    Convert.sortMyEvents(it, viewModel.allEvents.value)
                )
            )
        })
        viewModel.allEvents.observe(viewLifecycleOwner, Observer {
            fancyEventsAdapter.submitList(
                Convert.sortSavedEvents(
                    viewModel.tasks.value,
                    Convert.sortMyEvents(viewModel.userClubs.value, it)
                )
            )
        })
        viewModel.tasks.observe(viewLifecycleOwner, Observer {
            fancyEventsAdapter.submitList(
                Convert.sortSavedEvents(
                    it,
                    Convert.sortMyEvents(viewModel.userClubs.value, viewModel.allEvents.value)
                )
            )
        })


        (activity as MainActivity).setToolbar(binding.toolbar)


        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val width = displayMetrics.widthPixels
        LinearSnapHelper().attachToRecyclerView(binding.eventsRecyclerView)
        binding.eventsRecyclerView.layoutManager =
            EventLayoutManager(
                context!!
            )
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
