package com.naltynbekkz.nulife.ui.clubs.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Event
import com.naltynbekkz.nulife.ui.clubs.adapter.EventDaysAdapter
import com.naltynbekkz.nulife.ui.clubs.viewmodel.EventsViewModel
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment(private val all: Boolean) : Fragment() {

    lateinit var adapter: EventDaysAdapter
    lateinit var viewModel: EventsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = EventDaysAdapter(
            click = fun(event: Event) {
                //TODO: Navigate to eventfragment
//                val intent = Intent(context, EventActivity::class.java)
//                intent.putExtra("id", event.id)
//                startActivity(intent)
            }
        )

        viewModel = (activity as EventsActivity).eventsViewModel
        viewModel.tasks.observe(this@EventsFragment, Observer {
            adapter.setSavedData(it)
        })

        if (all) {
            viewModel.allEvents.observe(this, Observer {
                adapter.setData(it)
            })
        } else {
            viewModel.allEvents.observe(this@EventsFragment, Observer {
                adapter.setData(Convert.sortMyEvents(viewModel.userClubs.value, it))
            })
            viewModel.userClubs.observe(this@EventsFragment, Observer {
                adapter.setData(Convert.sortMyEvents(it, viewModel.allEvents.value))
            })
        }

//        viewModel.userClubs.observe(this, Observer {
//            userClubsAdapter.submitList(it)
//            fancyEventsAdapter.submitList(Convert.sortSavedEvents(viewModel.tasks.value, Convert.sortMyEvents(it, viewModel.allEvents.value)))
//        })
//        viewModel.allEvents.observe(this, Observer {
//            Convert.sortSavedEvents(viewModel.tasks.value, Convert.sortMyEvents(viewModel.userClubs.value, it))
//        })
//        viewModel.tasks.observe(this, Observer {
//            Convert.sortSavedEvents(it, Convert.sortMyEvents(viewModel.userClubs.value, viewModel.allEvents.value))
//        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_events, container, false)
    }

}