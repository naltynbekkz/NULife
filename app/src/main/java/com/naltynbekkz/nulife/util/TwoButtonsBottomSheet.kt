package com.naltynbekkz.nulife.util

import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import kotlinx.android.synthetic.main.bottom_sheet_two_buttons.view.*

class TwoButtonsBottomSheet(
    val first: () -> Unit,
    val second: () -> Unit,
    val firstText: String,
    val secondText: String
) : BottomSheetDialogFragment() {

    override fun setupDialog(dialog: Dialog, style: Int) {
        dialog.setContentView(View.inflate(context, R.layout.bottom_sheet_two_buttons, null).apply {
            first_button.text = firstText
            first_button.setOnClickListener {
                first()
                dismiss()
            }
            second_button.text = secondText
            second_button.setOnClickListener {
                second()
                dismiss()
            }
        })
    }

}