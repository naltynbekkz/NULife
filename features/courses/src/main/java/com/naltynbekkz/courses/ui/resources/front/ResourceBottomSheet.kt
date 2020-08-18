package com.naltynbekkz.courses.ui.resources.front

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.BottomSheetResourceBinding
import com.naltynbekkz.courses.model.Resource

class ResourceBottomSheet(
    private val resource: Resource,
    private val forceDownload: () -> Unit,
    private val openFolder: () -> Unit
) : BottomSheetDialogFragment() {

    lateinit var binding: BottomSheetResourceBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_resource, container, false)

        binding.resource = resource

        binding.forceDownload.setOnClickListener {
            forceDownload()
            dismiss()
        }
        binding.openFolder.setOnClickListener {
            openFolder()
            dismiss()
        }

        return binding.root

    }

}