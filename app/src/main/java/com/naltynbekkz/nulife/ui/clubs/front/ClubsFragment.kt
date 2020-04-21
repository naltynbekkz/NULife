package com.naltynbekkz.nulife.ui.clubs.front

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.ui.clubs.adapter.UserClubsAdapter
import kotlinx.android.synthetic.main.fragment_clubs.*

class ClubsFragment(private val all: Boolean) : Fragment() {

    lateinit var adapter: UserClubsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = UserClubsAdapter(
            click = fun(id, view) {
                // TODO navigate to club activity
//                val intent = Intent(context, ClubActivity::class.java)
//                intent.putExtra("id", id)
//                startActivity(intent)
            }
        )

        if (all) {
            (activity as ClubsActivity).clubsViewModel.allClubs
        } else {
            (activity as ClubsActivity).clubsViewModel.myClubs
        }.observe(this@ClubsFragment, Observer {
            adapter.submitList(it)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_clubs, container, false)
    }
}