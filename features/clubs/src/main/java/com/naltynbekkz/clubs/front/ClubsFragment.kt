package com.naltynbekkz.clubs.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.clubs.adapter.UserClubsAdapter
import com.naltynbekkz.clubs.databinding.FragmentClubsBinding
import com.naltynbekkz.clubs.viewmodel.ClubsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ClubsFragment : Fragment() {

    private val viewModel: ClubsViewModel by viewModels()

    private lateinit var binding: FragmentClubsBinding

    lateinit var adapter: UserClubsAdapter

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

        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentClubsBinding.inflate(inflater, container, false)
        return binding.root
    }
}