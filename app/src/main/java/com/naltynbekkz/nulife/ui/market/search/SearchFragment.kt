package com.naltynbekkz.nulife.ui.market.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.adapter.MarketSearchAdapter
import com.naltynbekkz.nulife.ui.market.front.ItemActivity
import com.naltynbekkz.nulife.ui.market.front.MyItemActivity
import com.naltynbekkz.nulife.ui.market.viewmodel.MarketViewModel
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment() {
    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    val viewModel: MarketViewModel by viewModels { viewModelProvider }
    lateinit var adapter: MarketSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.items.observe(this, Observer {
            adapter.setData(Convert.getAllItems(it, viewModel.femaleItems.value))
        })
        viewModel.femaleItems.observe(this, Observer {
            adapter.setData(Convert.getAllItems(viewModel.items.value, it))
        })

        viewModel.savedItems.observe(this, Observer {
            adapter.setFavoriteData(it)
        })

        adapter = MarketSearchAdapter(
            click = fun(item: Item) {
                val user = viewModel.user.value!!
                val intent = Intent(
                    context,
                    if (item.author.id == user.uid ||
                        item.author.id == user.anonymousId
                    ) {
                        MyItemActivity::class.java
                    } else {
                        ItemActivity::class.java
                    }
                )
                intent.putExtra("item", item)
                startActivity(intent)
            },
            like = viewModel::insert,
            dislike = viewModel::delete
//            getRating = viewModel::getRating
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            hideKeyboard()

            (activity as AppCompatActivity).onBackPressed()
        }

        recycler_view.adapter = adapter

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.setQuery(newText ?: "")
                return true
            }

        })

        fab.setOnClickListener {
            FilterBottomSheet(
                filter = adapter.filter,
                click = fun(filter: Filter) {
                    adapter.filter(filter)
                }
            ).show(parentFragmentManager, "tag")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun hideKeyboard() {
        (activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
            view!!.rootView!!.windowToken,
            0
        )
    }

}