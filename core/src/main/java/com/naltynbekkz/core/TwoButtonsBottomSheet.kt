package com.naltynbekkz.core

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.core.databinding.BottomSheetTwoButtonsBinding

class TwoButtonsBottomSheet(
    val first: () -> Unit,
    val second: () -> Unit,
    val firstText: String,
    val secondText: String
) : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetTwoButtonsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetTwoButtonsBinding.inflate(inflater, container, false)
        binding.firstButton.text = firstText
        binding.firstButton.setOnClickListener {
            first()
            dismiss()
        }
        binding.secondButton.text = secondText
        binding.secondButton.setOnClickListener {
            second()
            dismiss()
        }
        return binding.root
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        dialog.setContentView(View.inflate(context, R.layout.bottom_sheet_two_buttons, null).apply {

        })
    }

}