package com.naltynbekkz.nulife.ui.profile.front

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentCategoriesBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Category
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.profile.adapter.CategoriesAdapter
import com.naltynbekkz.nulife.ui.profile.viewmodel.CategoriesViewModel
import javax.inject.Inject

class CategoriesFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val viewModel: CategoriesViewModel by viewModels { viewModelProvider.create(this) }
    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var adapter: CategoriesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).profileComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        adapter = CategoriesAdapter(
            follow = viewModel::follow
        )
        viewModel.categories.observe(viewLifecycleOwner, Observer { following ->

            adapter.submitList(ArrayList<Category>().apply {
                resources.getStringArray(R.array.categories).forEach {
                    add(Category(it, following.contains(it.toLowerCase())))
                }
                resources.getStringArray(R.array.categories_extra).forEach {
                    add(Category(it, following.contains(it.toLowerCase())))
                }
            })

        })

        binding.recyclerView.adapter = adapter
        return binding.root
    }
}