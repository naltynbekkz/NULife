package com.naltynbekkz.nulife.ui.market.front

import android.view.MenuItem
import android.widget.Toast
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.util.Convert

class EditItemActivity : NewItemActivity() {

    override fun init() {
        binding.item = intent.extras!!.get("item") as Item
        binding.negotiable = (binding.item!!.discountedPrice == -1L)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {

                if (binding.item!!.isValid()) {
                    binding.loading = true

                    viewModel.edit(
                        item = binding.item!!.apply {
                            if (binding.negotiable!!) {
                                discountedPrice = -1
                            }
                            contacts = contactsAdapter.included
                            lastActive = System.currentTimeMillis() / 1000
                        },
                        anonymous = binding.anonymousSwitch.isChecked,
                        success = ::finish,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                this,
                                "Something went wrong. Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                } else {
                    binding.loading = false
                    if (binding.item!!.category.isEmpty()) {
                        Toast.makeText(
                            this,
                            getString(R.string.select_a_category),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            R.id.help -> {
                Convert.help(this)
            }
        }
        return true
    }

}