package com.naltynbekkz.courses.ui.courses.front

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.core.BottomNavigationController
import com.naltynbekkz.courses.databinding.FragmentEnrollBinding
import com.naltynbekkz.courses.ui.courses.adapter.EnrollAdapter
import com.naltynbekkz.courses.ui.courses.viewmodel.EnrollViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnrollFragment : Fragment() {

    private val viewModel: EnrollViewModel by viewModels()

    private lateinit var enrollAdapter: EnrollAdapter

    private lateinit var binding: FragmentEnrollBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnrollBinding.inflate(inflater, container, false)

        (activity as BottomNavigationController.HidingBottomNav).hideBottomNav()

        enrollAdapter = EnrollAdapter {
            findNavController().navigate(
                EnrollFragmentDirections.actionEnrollFragmentToEnrollCourseFragment(
                    it
                )
            )
        }

        viewModel.userCourses.observe(viewLifecycleOwner, Observer {
            enrollAdapter.setEnrolledData(it)
        })

        viewModel.courses.observe(viewLifecycleOwner, Observer {
            enrollAdapter.setData(it)
        })

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    activity!!.currentFocus!!.windowToken,
                    0
                )
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                enrollAdapter.filter.filter(newText ?: "")
                return true
            }

        })

        binding.recyclerView.adapter = enrollAdapter


        return binding.root
    }


}