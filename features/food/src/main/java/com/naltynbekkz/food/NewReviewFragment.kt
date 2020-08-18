package com.naltynbekkz.food

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.core.BottomNavigationController
import com.naltynbekkz.core.Constants.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.core.Constants.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.ImagesAdapter
import com.naltynbekkz.food.databinding.FragmentNewReviewBinding
import com.naltynbekkz.food.model.Review
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewReviewFragment : Fragment() {

    private val args: NewReviewFragmentArgs by navArgs()
    private val viewModel: NewReviewViewModel by viewModels()

    private lateinit var binding: FragmentNewReviewBinding
    private lateinit var adapter: ImagesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewReviewBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        (activity as BottomNavigationController.HidingBottomNav).hideBottomNav()

        binding.review = Review()

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.review!!.rating = args.rating

        adapter = ImagesAdapter(::selectImages)
        binding.recyclerView.adapter = adapter

        binding.ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            binding.review!!.rating = rating.toLong()
        }

        return binding.root
    }

    private fun selectImages() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
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
                .maxSelectable(1)
                .autoHideToolbarOnSingleTap(true)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)
        }
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
            adapter.setData(Matisse.obtainResult(data) as ArrayList<Uri>)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.naltynbekkz.core.R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.naltynbekkz.core.R.id.save -> {
                if (binding.review!!.rating != 0L) {
                    binding.loading = true
                    adapter.setState(0)

                    viewModel.post(
                        review = binding.review!!,
                        success = findNavController()::navigateUp,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                context,
                                "Something went wrong. Try again",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        images = adapter.images,
                        callback = fun(position: Int) {
                            adapter.setState(position + 1)
                        }
                    )
                } else {
                    binding.loading = false
                }
            }
            com.naltynbekkz.core.R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }
}