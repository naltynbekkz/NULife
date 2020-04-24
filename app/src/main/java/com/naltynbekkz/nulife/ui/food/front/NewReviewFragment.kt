package com.naltynbekkz.nulife.ui.food.front

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentNewReviewBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.Review
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.food.viewmodel.NewReviewViewModel
import com.naltynbekkz.nulife.util.Constant.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.nulife.util.Constant.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.nulife.util.Convert
import com.naltynbekkz.nulife.util.ImagesAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import javax.inject.Inject

class NewReviewFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: NewReviewFragmentArgs by navArgs()
    private val viewModel: NewReviewViewModel by viewModels {
        viewModelProvider.create(
            this,
            args.toBundle()
        )
    }

    private lateinit var binding: FragmentNewReviewBinding
    private lateinit var adapter: ImagesAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).foodComponent.inject(this)
    }

    override fun onDetach() {
        super.onDetach()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_review, container, false)
        setHasOptionsMenu(true)

        binding.review = Review()

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
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
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.review!!.rating != 0L) {
                    binding.loading = true
                    adapter.setState(0)


                    viewModel.post(
                        review = binding.review!!,
                        success = requireActivity()::onBackPressed,
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
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }
}