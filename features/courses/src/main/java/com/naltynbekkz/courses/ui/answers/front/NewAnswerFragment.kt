package com.naltynbekkz.courses.ui.answers.front

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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.core.Constants.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.core.Constants.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.core.Convert
import com.naltynbekkz.core.ImagesAdapter
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentNewAnswerBinding
import com.naltynbekkz.courses.ui.answers.viewmodel.NewAnswerViewModel
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class NewAnswerFragment : Fragment() {

    val viewModel: NewAnswerViewModel by viewModels()

    private lateinit var adapter: ImagesAdapter
    lateinit var binding: FragmentNewAnswerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_answer, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
        if (viewModel.answer.id.isEmpty()) {
            initNew()
        } else {
            initEdit()
        }
    }

    private fun initNew() {

        binding.answer = viewModel.answer

        adapter = ImagesAdapter(::selectImages)
        binding.recyclerView.adapter = adapter

    }

    private fun initEdit() {

        binding.uploadImage.apply {
            alpha = 0.5F
            isEnabled = false
        }
        binding.answer = viewModel.answer
        binding.anonymousSwitch.isChecked = (viewModel.answer.author.name == "Anonymous")
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    selectImages()
                }
                return
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            adapter.setData(Matisse.obtainResult(data) as ArrayList<Uri>)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                if (binding.answer!!.isValid() && binding.loading != true) {
                    binding.loading = true

                    if (viewModel.answer.id.isEmpty()) {
                        adapter.setState(0)

                        viewModel.answer(
                            anonymous = binding.anonymousSwitch.isChecked,
                            failure = fun() {
                                binding.loading = null
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            images = adapter.images,
                            success = findNavController()::navigateUp,
                            done = fun(position: Int) {
                                adapter.setState(position + 1)
                            }
                        )

                    } else {
                        viewModel.editAnswer(
                            anonymous = binding.anonymousSwitch.isChecked,
                            failure = fun() {
                                binding.loading = null
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            success = findNavController()::navigateUp
                        )
                    }

                }
            }
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
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
                .maxSelectable(5)
                .autoHideToolbarOnSingleTap(true)
                .thumbnailScale(0.85f)
                .imageEngine(GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE)
        }
    }


}
