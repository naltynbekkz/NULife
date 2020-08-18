package com.naltynbekkz.profile.front

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.naltynbekkz.core.Constants.Companion.PERMISSION_REQUEST_CODE
import com.naltynbekkz.core.Constants.Companion.REQUEST_CODE_CHOOSE
import com.naltynbekkz.core.Convert
import com.naltynbekkz.profile.R
import com.naltynbekkz.profile.databinding.FragmentEditProfileBinding
import com.naltynbekkz.profile.viewmodel.EditProfileViewModel
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.engine.impl.GlideEngine
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment() {

    private val viewModel: EditProfileViewModel by viewModels()

    private lateinit var binding: FragmentEditProfileBinding

    private lateinit var yearAdapter: ArrayAdapter<Long>
    private lateinit var schoolAdapter: ArrayAdapter<String>
    private lateinit var majorAdapter: ArrayAdapter<String>

    private var imageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        (activity as AppCompatActivity).apply {
            setSupportActionBar(binding.toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            binding.toolbar.setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }

        binding.avatarImage.setOnClickListener {
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
                selectImages()
            }
        }

        yearAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_spinner_layout,
                arrayOf(1L, 2L, 3L, 4L, 5L)
            )
        schoolAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_spinner_layout,
            resources.getStringArray(R.array.schools)
        )
        binding.year.adapter = yearAdapter
        binding.school.adapter = schoolAdapter

        binding.year.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.user?.let {
                    it.year = yearAdapter.getItem(position)
                }
            }

        }

        binding.school.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.user?.let {
                    it.school = schoolAdapter.getItem(position)
                    majorAdapter = ArrayAdapter(
                        requireContext(),
                        R.layout.item_spinner_layout,
                        resources.getStringArray(
                            when (position) {
                                0 -> R.array.graduate_school_of_business
                                1 -> R.array.graduate_school_of_public_policy
                                2 -> R.array.school_of_engineering_and_digital_sciences
                                3 -> R.array.school_of_medicine
                                4 -> R.array.school_of_mining_and_geosciences
                                else -> R.array.school_of_sciences_and_humanities
                            }
                        )
                    )
                    binding.major.adapter = majorAdapter
                    if (it.major == null) {
                        it.major = majorAdapter.getItem(0)
                    } else {
                        for (j in 0 until majorAdapter.count) {
                            if (it.major == majorAdapter.getItem(j)) {
                                binding.major.setSelection(j)
                            }
                        }
                    }

                }

            }

        }

        binding.major.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                binding.user!!.major = majorAdapter.getItem(position)
            }

        }


        viewModel.user.observe(viewLifecycleOwner, Observer { user ->
            binding.user = user

            user.school?.let { school ->
                for (i in 0 until schoolAdapter.count) {
                    if (school == schoolAdapter.getItem(i)) {
                        binding.school.setSelection(i)

                        majorAdapter = ArrayAdapter(
                            requireContext(),
                            R.layout.item_spinner_layout,
                            resources.getStringArray(
                                when (i) {
                                    0 -> R.array.graduate_school_of_business
                                    1 -> R.array.graduate_school_of_public_policy
                                    2 -> R.array.school_of_engineering_and_digital_sciences
                                    3 -> R.array.school_of_medicine
                                    4 -> R.array.school_of_mining_and_geosciences
                                    else -> R.array.school_of_sciences_and_humanities
                                }
                            )
                        )
                        binding.major.adapter = majorAdapter
                        user.major?.let { major ->
                            for (j in 0 until majorAdapter.count) {
                                if (major == majorAdapter.getItem(j)) {
                                    binding.major.setSelection(j)
                                }
                            }
                        }
                    }
                }
            }

            user.year?.let {
                for (i in 0 until yearAdapter.count) {
                    if (it == yearAdapter.getItem(i)) {
                        binding.year.setSelection(i)
                    }
                }
            }

        })


        return binding.root
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
            imageUri = (Matisse.obtainResult(data) as ArrayList<Uri>).firstOrNull()
            Glide.with(this).load(imageUri).into(binding.avatarImage)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save -> {
                binding.loading = true
                viewModel.editProfile(
                    user = binding.user!!,
                    image = imageUri,
                    success = findNavController()::navigateUp,
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
            R.id.help -> {
                Convert.help(requireContext())
            }
        }
        return true
    }

    private fun selectImages() {
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