package com.naltynbekkz.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.core.databinding.BottomSheetEditDeleteBinding

class EditDeleteBottomSheet(
    private val edit: () -> Unit,
    private val delete: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetEditDeleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetEditDeleteBinding.inflate(inflater, container, false)
        binding.editButton.setOnClickListener {
            dismiss()
            edit()
        }
        binding.deleteButton.setOnClickListener {
            dismiss()
            delete()
        }
        return binding.root
    }

}