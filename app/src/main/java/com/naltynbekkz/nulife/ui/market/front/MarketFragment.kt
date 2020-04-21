package com.naltynbekkz.nulife.ui.market.front

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.market.adapter.MarketAdapter
import com.naltynbekkz.nulife.ui.market.viewmodel.MarketViewModel
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.fragment_market.*
import javax.inject.Inject

class MarketFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    val viewModel: MarketViewModel by viewModels { viewModelProvider.create(this) }

    lateinit var adapter: MarketAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        viewModel.items.observe(this, Observer {
            adapter.setData(Convert.getAllItems(it, viewModel.femaleItems.value))
        })
        viewModel.femaleItems.observe(this, Observer {
            adapter.setData(Convert.getAllItems(viewModel.items.value, it))
        })
        viewModel.savedItems.observe(this, Observer {
            adapter.setSavedData(it)
        })

        adapter = MarketAdapter(
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
        )

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setToolbar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(context, NewItemActivity::class.java)
            startActivity(intent)
        }
        recycler_view.adapter = adapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
//                findNavController().navigate(R.id.action_marketFragment_to_searchFragment)
            }
            R.id.favorites -> {
                val intent = Intent(context, MyItemsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.market_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

}
