package com.naltynbekkz.nulife.util.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.FooterNewContactsBinding
import com.naltynbekkz.nulife.databinding.ItemNewContactBinding
import com.naltynbekkz.nulife.model.Contact

class NewContactsAdapter(
    private var new: () -> Unit,
    private var remove: (Contact) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var contacts: List<Contact> = ArrayList()

    fun setData(contacts: List<Contact>) {
        this.contacts = contacts
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        if (viewType == BODY) {
            return ContactViewHolder(
                ItemNewContactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            return FooterViewHolder(
                FooterNewContactsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            holder.bind(contacts[position], remove)
        } else if (holder is FooterViewHolder) {
            holder.bind(new)
        }
    }

    override fun getItemViewType(position: Int) = if (position == contacts.size) FOOTER else BODY

    override fun getItemCount() = contacts.size + 1

    class ContactViewHolder(private val binding: ItemNewContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            contact: Contact,
            remove: (Contact) -> Unit
        ) {
            binding.contact = contact
            binding.remove.setOnClickListener {
                remove(contact)
            }
        }
    }

    class FooterViewHolder(private val binding: FooterNewContactsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            new: () -> Unit
        ) {
            binding.root.setOnClickListener {
                new()
            }
        }
    }

    companion object {
        const val FOOTER = 0
        const val BODY = 1
    }
}