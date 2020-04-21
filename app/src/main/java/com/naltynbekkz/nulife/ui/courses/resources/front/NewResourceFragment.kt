package com.naltynbekkz.nulife.ui.courses.resources.front

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.FragmentNewResourceBinding
import com.naltynbekkz.nulife.di.ViewModelProviderFactory
import com.naltynbekkz.nulife.model.LocalFile
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.ui.MainActivity
import com.naltynbekkz.nulife.ui.courses.resources.adapter.FilesAdapter
import com.naltynbekkz.nulife.ui.courses.resources.viewmodel.NewResourceViewModel
import com.naltynbekkz.nulife.util.Convert
import javax.inject.Inject

class NewResourceFragment : Fragment() {

    @Inject
    lateinit var viewModelProvider: ViewModelProviderFactory
    private val args: NewResourceFragmentArgs by navArgs()
    private val viewModel: NewResourceViewModel by viewModels {
        viewModelProvider.create(this, args.toBundle())
    }

    private lateinit var adapter: FilesAdapter
    private lateinit var docIntent: Intent
    private lateinit var binding: FragmentNewResourceBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).coursesComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_new_resource, container, false)
        setHasOptionsMenu(true)
        return binding.root
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

        binding.resource = Resource(viewModel.userCourse)


        val mimeTypes = arrayOf(
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/pdf"
        )

        docIntent = Intent(Intent.ACTION_GET_CONTENT)
        docIntent.addCategory(Intent.CATEGORY_OPENABLE)
        docIntent.type = if (mimeTypes.size == 1) mimeTypes[0] else "*/*"
        docIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

        adapter =
            FilesAdapter {
                startActivityForResult(
                    Intent.createChooser(docIntent, "ChooseFile"),
                    REQUEST_CODE_DOC
                )
            }
        binding.recyclerView.adapter = adapter
        binding.semesterSpinner.setAdapter(
            ArrayAdapter(
                requireContext(), R.layout.item_spinner_layout, resources.getStringArray(
                    R.array.semesters
                )
            )
        )

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).hideBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as MainActivity).showBottomNavigation()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_DOC) {

                data!!.data?.let {
                    requireActivity().contentResolver.query(it, null, null, null, null)
                }?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    adapter.setData(ArrayList<LocalFile>().apply {
                        add(LocalFile(data.data!!, cursor.getString(nameIndex)))
                    })
                    cursor.close()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {

                if (
                    adapter.files.isNotEmpty() &&
                    binding.resource!!.semester.isNotEmpty() &&
                    binding.resource!!.title.isNotEmpty()
                ) {
                    binding.loading = true
                    adapter.setState(0)

                    viewModel.post(
                        resource = binding.resource!!.apply {
                            year = binding.yearEditText.text.toString().toLong()
                            contentType = adapter.files[0].suffix
                        },
                        success = requireActivity()::onBackPressed,
                        failure = fun() {
                            binding.loading = null
                            Toast.makeText(
                                requireContext(),
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        done = fun(position: Int) {
                            adapter.setState(position + 1)
                        },
                        files = adapter.files
                    )
                } else {
                    binding.loading = false
                }
                return true
            }
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return false
    }

    companion object {
        private const val REQUEST_CODE_DOC = 2222
    }

}
















