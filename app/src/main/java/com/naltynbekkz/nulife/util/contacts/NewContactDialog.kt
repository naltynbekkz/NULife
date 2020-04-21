package com.naltynbekkz.nulife.util.contacts

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.databinding.DialogNewContactBinding
import com.naltynbekkz.nulife.model.Contact

class NewContactDialog(
    val accept: (Contact) -> Unit
) : AppCompatDialogFragment() {

    private lateinit var binding: DialogNewContactBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_new_contact,
            null,
            false
        )
        binding.typeSpinner.setAdapter(
            ArrayAdapter(
                context!!,
                R.layout.item_spinner_layout,
                resources.getStringArray(R.array.contact_type_array)
            )
        )
        binding.contact = Contact()

        return AlertDialog.Builder(activity).setView(binding.root)
            .setTitle(R.string.new_contact)
            .setPositiveButton(R.string.accept) { _, _ ->
                if (binding.contact!!.type.isNotEmpty() && binding.contact!!.data.isNotEmpty()) {
                    accept(binding.contact!!)
                }
            }.create()
    }
}
