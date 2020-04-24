package com.naltynbekkz.nulife.ui.market.front

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.ActivityNewItemBinding
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.viewmodel.NewItemViewModel
import com.naltynbekkz.nulife.util.Constant.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.nulife.util.Constant.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.nulife.util.Convert
import com.naltynbekkz.nulife.util.ImagesAdapter
import com.naltynbekkz.nulife.util.contacts.ChooseContactsAdapter
import com.naltynbekkz.nulife.util.contacts.NewContactDialog
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import javax.inject.Inject

open class NewItemActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    lateinit var binding: ActivityNewItemBinding
    val viewModel: NewItemViewModel by viewModels { viewModelProvider }
    lateinit var contactsAdapter: ChooseContactsAdapter

    private lateinit var imagesAdapter: ImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_item)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        binding.user = viewModel.user.value!!

        contactsAdapter =
            ChooseContactsAdapter {
                NewContactDialog(accept = viewModel::insert)
                    .show(
                        supportFragmentManager,
                        "EditProfileActivity"
                    )
            }
        binding.contactsRecyclerView.adapter = contactsAdapter

        resources.getStringArray(R.array.categories).forEach {
            val mChip = layoutInflater.inflate(
                R.layout.item_chip_category,
                binding.categories,
                false
            ) as Chip
            mChip.text = it
            mChip.setOnCheckedChangeListener { button, b ->
                if (b) {
                    binding.item!!.category = button.text.toString()
                }
            }
            binding.categories.addView(mChip)
        }

        viewModel.contacts.observe(this, Observer {
            contactsAdapter.setData(it)
        })

        init()
    }

    open fun init() {
        binding.item = Item()
        binding.negotiable = false
        imagesAdapter = ImagesAdapter(::selectImages)
        binding.recyclerView.adapter = imagesAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    selectImages()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            imagesAdapter.setData(Matisse.obtainResult(data) as ArrayList<Uri>)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.save_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.item!!.isValid()) {
                    binding.loading = true

                    imagesAdapter.setState(0)

                    viewModel.post(
                        item = binding.item!!.apply {
                            if (binding.negotiable!!) {
                                price = -1
                            }
                            discountedPrice = price
                            contacts = contactsAdapter.included
                            timestamp = System.currentTimeMillis() / 1000
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
                        },
                        images = imagesAdapter.images,
                        done = fun(position: Int) {
                            imagesAdapter.setState(position + 1)
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

    private fun selectImages() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        } else {
            Matisse.from(this)
                .choose(MimeType.of(MimeType.JPEG))
                .countable(true)
                .maxSelectable(5)
                .autoHideToolbarOnSingleTap(true)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)
        }
    }

}
