package com.naltynbekkz.clubs.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.clubs.Sorting
import com.naltynbekkz.clubs.adapter.EventDaysAdapter
import com.naltynbekkz.clubs.databinding.FragmentEventsBinding
import com.naltynbekkz.clubs.viewmodel.EventsViewModel
import com.naltynbekkz.core.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private val viewModel: EventsViewModel by viewModels()

    private lateinit var binding: FragmentEventsBinding

    lateinit var adapter: EventDaysAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = EventDaysAdapter(
            click = fun(id: String) {
                findNavController().navigate(
                    AllEventsFragmentDirections.actionAllEventsFragmentToEventFragment(id)
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.events.observe(viewLifecycleOwner, Observer {
            adapter.submitList(
                Sorting.eventsToDays(
                    Sorting.sortSavedEvents(
                        it.first,
                        if (requireArguments().getBoolean(Constants.ALL))
                            it.third
                        else
                            Sorting.sortMyEvents(it.second, it.third)
                    )
                )
            )
        })

        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

}