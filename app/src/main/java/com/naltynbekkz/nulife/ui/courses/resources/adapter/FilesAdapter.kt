package com.naltynbekkz.nulife.ui.courses.resources.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.FooterFilesBinding
import com.naltynbekkz.nulife.databinding.ItemFileBinding
import com.naltynbekkz.nulife.model.LocalFile

class FilesAdapter(
    val add: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val FILE = 0
        const val FOOTER = 1
    }

    var files: ArrayList<LocalFile> = ArrayList()
    private var state: Int? = null

    fun setState(state: Int?) {
        this.state = state
        notifyDataSetChanged()
    }

    fun setData(files: ArrayList<LocalFile>) {
        this.files = files
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == FILE) {
            ItemViewHolder(
                ItemFileBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            FooterViewHolder(
                FooterFilesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == files.size) FOOTER else FILE
    }

    override fun getItemCount() = if (state == null) files.size + 1 else files.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(files[position], state, fun(i: Int) {
                files.removeAt(i)
                notifyDataSetChanged()
            }, position)
        } else if (holder is FooterViewHolder) {
            holder.bind(add)
        }
    }


    class ItemViewHolder(val binding: ItemFileBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            file: LocalFile,
            state: Int?,
            remove: (Int) -> Unit,
            position: Int
        ) {
            binding.file = file
            binding.uploading = if (state == null) null else state <= position
            binding.cancel.setOnClickListener {
                remove(position)
            }
        }
    }

    class FooterViewHolder(val binding: FooterFilesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            add: () -> Unit
        ) {
            binding.root.setOnClickListener {
                add()
            }
        }
    }

}