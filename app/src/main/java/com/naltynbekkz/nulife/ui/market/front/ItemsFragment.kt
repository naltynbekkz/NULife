package com.naltynbekkz.nulife.ui.market.front

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.adapter.MarketAdapter
import com.naltynbekkz.nulife.ui.market.viewmodel.MyItemsViewModel
import kotlinx.android.synthetic.main.fragment_items.*

class ItemsFragment(val saved: Boolean) : Fragment() {

    lateinit var adapter: MarketAdapter
    lateinit var viewModel: MyItemsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MyItemsActivity).viewModel

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
//            getRating = viewModel::getRating
        )



        if (saved) {
            viewModel.savedItems.observe(this@ItemsFragment, Observer {
                adapter.setData(it)
                adapter.setSavedData(it)
            })
        } else {
            viewModel.items.observe(this@ItemsFragment, Observer {
                adapter.setData(it)
            })
            viewModel.savedItems.observe(this@ItemsFragment, Observer {
                adapter.setSavedData(it)
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

}