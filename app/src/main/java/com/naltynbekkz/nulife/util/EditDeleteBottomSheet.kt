package com.naltynbekkz.nulife.util

import android.app.Dialog
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naltynbekkz.nulife.R
import kotlinx.android.synthetic.main.bottom_sheet_edit_delete.view.*

class EditDeleteBottomSheet(
    private val edit: () -> Unit,
    private val delete: () -> Unit
) : BottomSheetDialogFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.bottom_sheet_edit_delete, null)
        with(view) {
            edit_button.setOnClickListener {
                dismiss()
                edit()
            }
            delete_button.setOnClickListener {
                dismiss()
                delete()
            }
        }
        dialog.setContentView(view)
    }
}