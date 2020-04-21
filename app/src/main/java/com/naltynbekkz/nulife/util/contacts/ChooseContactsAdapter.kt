package com.naltynbekkz.nulife.util.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.*
import com.naltynbekkz.nulife.model.Contact

class ChooseContactsAdapter(
    val new: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val ITEM = 0
        const val HEADER = 1
        const val SEPARATOR = 2
        const val EMPTY = 3
        const val FOOTER = 4
    }

    var included = ArrayList<Contact>()
    var excluded = ArrayList<Contact>()

    fun setData(list: List<Contact>) {
        val contacts = ArrayList<Contact>(list)
        contacts.removeAll(included)
        contacts.removeAll(excluded)
        if (contacts.size == 1) {
            included.addAll(contacts)
        } else {
            excluded.addAll(contacts)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM -> ItemViewHolder(
                ItemChooseContactBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            HEADER -> HeaderViewHolder(
                HeaderChooseContactsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            SEPARATOR -> SeparatorViewHolder(
                SeparatorChooseContactsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            EMPTY -> EmptyViewHolder(
                ItemEmptyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> FooterViewHolder(
                FooterNewContactsBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (included.isEmpty() && excluded.isEmpty()) {
            when (position) {
                0 -> HEADER
                1 -> EMPTY
                else -> FOOTER
            }
        } else if (included.isEmpty() && excluded.isNotEmpty()) {
            when (position) {
                0 -> HEADER
                1 -> EMPTY
                2 -> SEPARATOR
                3 + excluded.size -> FOOTER
                else -> ITEM
            }
        } else if (included.isNotEmpty() && excluded.isNotEmpty()) {
            when (position) {
                0 -> HEADER
                1 + included.size -> SEPARATOR
                2 + included.size + excluded.size -> FOOTER
                else -> ITEM
            }
        } else {
            when (position) {
                0 -> HEADER
                1 + included.size -> FOOTER
                else -> ITEM
            }
        }
    }

    override fun getItemCount(): Int {
        return if (included.isEmpty() && excluded.isEmpty()) {
            1 + 1 + 1
        } else if (included.isEmpty() && excluded.isNotEmpty()) {
            1 + 1 + 1 + excluded.size + 1
        } else if (included.isNotEmpty() && excluded.isNotEmpty()) {
            1 + included.size + 1 + excluded.size + 1
        } else {
            1 + included.size + 1
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FooterViewHolder) {
            holder.bind(new)
        }
        if (holder is ItemViewHolder) {
            holder.bind(
                position = position,
                chosen = if (included.isEmpty() && excluded.isNotEmpty()) {
                    false
                } else if (included.isNotEmpty() && excluded.isNotEmpty()) {
                    position < included.size + 1
                } else {
                    true
                },
                contact = if (included.isEmpty() && excluded.isNotEmpty()) {
                    excluded[position - 3]
                } else if (included.isNotEmpty() && excluded.isNotEmpty()) {
                    if (position < included.size + 1) {
                        included[position - 1]
                    } else {
                        excluded[position - included.size - 2]
                    }
                } else {
                    included[position - 1]
                },
                add = fun(i) {
                    if (included.isEmpty()) {
                        val contact = excluded[i - 3]
                        excluded.removeAt(i - 3)
                        included.add(contact)
                    } else {
                        val contact = excluded[i - included.size - 2]
                        excluded.removeAt(i - included.size - 2)
                        included.add(contact)
                    }
                    notifyDataSetChanged()
                },
                remove = fun(i) {
                    val contact = included[i - 1]
                    included.removeAt(i - 1)
                    excluded.add(contact)
                    notifyDataSetChanged()
                }
            )
        }
    }


    class EmptyViewHolder(val binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HeaderViewHolder(val binding: HeaderChooseContactsBinding) :
        RecyclerView.ViewHolder(binding.root)

    class SeparatorViewHolder(val binding: SeparatorChooseContactsBinding) :
        RecyclerView.ViewHolder(binding.root)

    class ItemViewHolder(val binding: ItemChooseContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            position: Int,
            chosen: Boolean,
            contact: Contact,
            add: (Int) -> Unit,
            remove: (Int) -> Unit
        ) {
            binding.contact = contact
            binding.chosen = chosen
            binding.root.setOnClickListener {
                if (chosen) remove(position) else add(position)
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

}