package com.naltynbekkz.nulife.ui.courses.questions.front

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentNewQuestionBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.questions.viewmodel.NewQuestionViewModel
import com.naltynbekkz.nulife.util.Constant.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.nulife.util.Constant.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.nulife.util.Convert
import com.naltynbekkz.nulife.util.ImagesAdapter
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import javax.inject.Inject

class NewQuestionFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: NewQuestionFragmentArgs by navArgs()
    val viewModel: NewQuestionViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    private lateinit var adapter: ImagesAdapter
    lateinit var binding: FragmentNewQuestionBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_question, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                onBackPressed()
            }
        }

        binding.question = viewModel.question

        if (viewModel.question.id.isEmpty()) {
            initNew()
        } else {
            initEdit()
        }
    }

    private fun initNew() {

        viewModel.topics.observe(viewLifecycleOwner, Observer {
            binding.topic.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    R.layout.item_spinner_layout,
                    it
                )
            )
        })

        adapter = ImagesAdapter(::selectImages)

        binding.recyclerView.adapter = adapter

    }

    private fun initEdit() {
        binding.toolbar.title = "Edit question"

        binding.allSectionsSwitch.isChecked = viewModel.question.sectionId == 0L
        binding.anonymousSwitch.isChecked = viewModel.question.author.name == "Anonymous"


        arrayListOf(
            binding.selectTopic,
            binding.topic,
            binding.allSections,
            binding.allSectionsSwitch,
            binding.uploadImage
        ).forEach {
            it.isEnabled = false
            it.alpha = 0.5F
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

                if (binding.question!!.isValid() && binding.loading != true) {
                    binding.loading = true

                    if (viewModel.question.id.isEmpty()) {

                        adapter.setState(0)

                        viewModel.post(
                            question = binding.question!!,
                            anonymous = binding.anonymousSwitch.isChecked,
                            allSections = binding.allSectionsSwitch.isChecked,
                            success = requireActivity()::onBackPressed,
                            failure = fun() {
                                binding.loading = null
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            images = adapter.images,
                            done = fun(position: Int) {
                                adapter.setState(position + 1)
                            }
                        )
                    } else {
                        viewModel.editQuestion(
                            question = binding.question!!,
                            anonymous = binding.anonymousSwitch.isChecked,
                            success = requireActivity()::onBackPressed,
                            failure = fun() {
                                binding.loading = null
                                Toast.makeText(
                                    requireContext(),
                                    "Something went wrong. Try again",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
