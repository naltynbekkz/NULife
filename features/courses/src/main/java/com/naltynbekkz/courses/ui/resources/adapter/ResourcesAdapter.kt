package com.naltynbekkz.courses.ui.resources.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.R
import com.naltynbekkz.courses.databinding.ItemResourceBinding
import com.naltynbekkz.courses.model.Resource
import com.naltynbekkz.courses.model.Resource.Companion.DONE
import com.naltynbekkz.courses.model.Resource.Companion.LOADING
import com.naltynbekkz.courses.model.Resource.Companion.PENDING

class ResourcesAdapter(
    var resources: ArrayList<Resource>,
    private val click: (Resource) -> Unit,
    private val download: (Resource) -> Unit
) :
    RecyclerView.Adapter<ResourcesAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount() = resources.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(resources[position], click, download)


    class ItemViewHolder(val binding: ItemResourceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(resource: Resource, click: (Resource) -> Unit, download: (Resource) -> Unit) {

            binding.title.text = resource.title
            if (resource.professor.isNullOrEmpty()) {
                binding.professor.visibility = View.INVISIBLE
                binding.dot.visibility = View.INVISIBLE
            } else {
                binding.professor.text = resource.professor
            }
            binding.semester.text = resource.semester

            binding.logo.background = binding.root.resources.getDrawable(
                when (resource.contentType) {
                    "pdf" -> R.drawable.ic_adobe
                    "doc", "docx" -> R.drawable.ic_word
                    else -> R.drawable.ic_image
                },
                binding.root.context.theme
            )

            binding.arrow.setOnClickListener {
                download(resource)
            }
            when (resource.status) {
                PENDING -> binding.arrow.visibility = View.VISIBLE
                LOADING -> binding.progressBar.visibility = View.VISIBLE
                DONE -> binding.check.visibility = View.VISIBLE
            }
            binding.root.setOnClickListener {
                click(resource)
            }
        }
    }

}