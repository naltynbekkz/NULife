package com.naltynbekkz.core

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.core.databinding.FooterImagesBinding
import com.naltynbekkz.core.databinding.ItemImageSmallBinding

class ImagesAdapter(
    val add: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val IMAGE = 0
        const val FOOTER = 1
    }

    var images: ArrayList<Uri> = ArrayList()
    private var state: Int? = null

    fun setState(state: Int?) {
        this.state = state
        notifyDataSetChanged()
    }

    fun setData(images: ArrayList<Uri>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        if (viewType == IMAGE) {
            ItemViewHolder(
                ItemImageSmallBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            FooterViewHolder(
                FooterImagesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

    override fun getItemViewType(position: Int): Int {
        return if (position == images.size) FOOTER else IMAGE
    }

    override fun getItemCount() = if (state == null) images.size + 1 else images.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(images[position], state, fun(i: Int) {
                images.removeAt(i)
                notifyDataSetChanged()
            }, position)
        } else if (holder is FooterViewHolder) {
            holder.bind(add)
        }
    }


    class ItemViewHolder(val binding: ItemImageSmallBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            imageUri: Uri,
            state: Int?,
            remove: (Int) -> Unit,
            position: Int
        ) {
            binding.uri = imageUri
            binding.uploading = if (state == null) null else state <= position
            binding.cancel.setOnClickListener {
                remove(position)
            }
        }
    }

    class FooterViewHolder(val binding: FooterImagesBinding) :
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