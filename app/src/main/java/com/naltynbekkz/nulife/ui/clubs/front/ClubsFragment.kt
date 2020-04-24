package com.naltynbekkz.nulife.ui.clubs.front

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.clubs.adapter.UserClubsAdapter
import com.naltynbekkz.nulife.ui.clubs.viewmodel.ClubsViewModel
import kotlinx.android.synthetic.main.fragment_clubs.*
import javax.inject.Inject

class ClubsFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: ClubsViewModel by viewModels {
        viewModelProvider.create(this, arguments)
    }

    lateinit var adapter: UserClubsAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).clubsComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = UserClubsAdapter(
            click = fun(id) {
                findNavController().navigate(
                    AllClubsFragmentDirections.actionAllClubsFragmentToClubFragment(id)
                )
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.clubs.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

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