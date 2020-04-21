package com.naltynbekkz.nulife.ui.market.front

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityItemBinding
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.viewmodel.ItemViewModel
import com.naltynbekkz.nulife.util.contacts.ChooseContactsAdapter
import com.naltynbekkz.nulife.util.contacts.NewContactDialog
import javax.inject.Inject

class ItemActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var binding: ActivityItemBinding
    private val viewModel: ItemViewModel by viewModels { viewModelProvider }
    private lateinit var adapter: ChooseContactsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item)

        val item = intent.extras!!.get("item") as Item

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        viewModel.savedItem.observe(this, Observer {
            invalidateOptionsMenu()
        })

        adapter =
            ChooseContactsAdapter {
                NewContactDialog(accept = viewModel::insert)
                    .show(
                        supportFragmentManager,
                        "EditProfileActivity"
                    )
            }

        binding.contactsRecyclerView.adapter = adapter

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            if (rating != 0f) {
                viewModel.rate(rating.toLong(), success = fun() {}, failure = fun() {})
            }
        }

        viewModel.contacts.observe(this, Observer { adapter.setData(it) })

        binding.send.setOnClickListener {
            if (adapter.included.isEmpty()) {
                Toast.makeText(this, "Please include at least on contact", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.request(adapter.included)
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu!!.getItem(0).apply {
            if (viewModel.savedItem.value!!.isEmpty()) {
                icon = resources.getDrawable(R.drawable.ic_favorite, theme)
                title = resources.getString(R.string.like)
            } else {
                icon = resources.getDrawable(R.drawable.ic_favorite_filled, theme)
                title = resources.getString(R.string.unlike)
            }
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                if (viewModel.savedItem.value!!.isEmpty()) {
                    item.icon = resources.getDrawable(R.drawable.ic_favorite_filled, theme)
                    item.title = resources.getString(R.string.unlike)
                    viewModel.insert(viewModel.itemLiveData.value!!)
                } else {
                    item.icon = resources.getDrawable(R.drawable.ic_favorite, theme)
                    item.title = resources.getString(R.string.like)
                    viewModel.delete(viewModel.itemLiveData.value!!.id)
                }
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.item_menu, menu)
        return true
    }


}