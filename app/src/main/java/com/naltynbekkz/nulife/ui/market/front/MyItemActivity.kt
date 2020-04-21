package com.naltynbekkz.nulife.ui.market.front

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityMyItemBinding
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.viewmodel.MyItemViewModel

class MyItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyItemBinding
    private lateinit var viewModel: MyItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_my_item)

        val item = intent.extras!!.get("item") as Item

        binding.toolbar.title = ""
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val intent = Intent(this, EditItemActivity::class.java)
                intent.putExtra("item", binding.viewModel!!.itemLiveData.value)
                startActivity(intent)
            }
            R.id.delete -> {

                MaterialAlertDialogBuilder(this)
                    .setTitle("Are you sure?")
                    .setPositiveButton("Delete") { _, _ ->
                        viewModel.delete(
                            ::finish,
                            fun() {
                                Toast.makeText(
                                    this,
                                    "Something went wrong. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                    .setNegativeButton("Cancel") { _, _ ->

                    }.show()
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.my_item_menu, menu)
        return true
    }
}