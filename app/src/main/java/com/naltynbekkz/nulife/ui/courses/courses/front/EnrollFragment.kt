package com.naltynbekkz.nulife.ui.courses.courses.front

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
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.courses.adapter.EnrollAdapter
import com.naltynbekkz.nulife.ui.courses.courses.viewmodel.EnrollViewModel
import kotlinx.android.synthetic.main.fragment_enroll.*
import javax.inject.Inject

class EnrollFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: EnrollViewModel by viewModels { viewModelProvider.create(this) }

    private lateinit var enrollAdapter: EnrollAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        recycler_view.adapter = enrollAdapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enroll, container, false)
    }


}