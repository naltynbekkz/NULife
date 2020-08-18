package com.naltynbekkz.courses.ui.resources.front

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.naltynbekkz.courses.ui.resources.adapter.FilesAdapter
import com.naltynbekkz.courses.ui.resources.viewmodel.NewResourceViewModel
import com.naltynbekkz.core.Convert
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.FragmentNewResourceBinding
import com.naltynbekkz.courses.model.LocalFile
import com.naltynbekkz.courses.model.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewResourceFragment : Fragment() {

    private val viewModel: NewResourceViewModel by viewModels()

    private lateinit var adapter: FilesAdapter
    private lateinit var docIntent: Intent
    private lateinit var binding: FragmentNewResourceBinding

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

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
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
                        success = findNavController()::navigateUp,
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
















