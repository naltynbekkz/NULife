package com.naltynbekkz.nulife.ui.courses.resources.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.model.Resource.Companion.DONE
import com.naltynbekkz.nulife.model.Resource.Companion.LOADING
import com.naltynbekkz.nulife.model.Resource.Companion.PENDING
import kotlinx.android.synthetic.main.item_resource.view.*

class ResourcesAdapter(
    var resources: ArrayList<Resource>,
    private val click: (Resource) -> Unit,
    private val download: (Resource) -> Unit
) :
    RecyclerView.Adapter<ResourcesAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_resource,
                parent,
                false
            )
        )

    override fun getItemCount() = resources.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(resources[position], click, download)


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(resource: Resource, click: (Resource) -> Unit, download: (Resource) -> Unit) =
            with(itemView) {

                title.text = resource.title
                if (resource.professor.isNullOrEmpty()) {
                    professor.visibility = View.INVISIBLE
                    dot.visibility = View.INVISIBLE
                } else {
                    professor.text = resource.professor
                }
                semester.text = resource.semester

                logo.background = when (resource.contentType) {
                    "pdf" -> resources.getDrawable(R.drawable.ic_adobe, context.theme)
                    "doc", "docx" -> resources.getDrawable(R.drawable.ic_word, context.theme)
                    else -> resources.getDrawable(R.drawable.ic_image, context.theme)
                }

                arrow.setOnClickListener {
                    download(resource)
                }
                when (resource.status) {
                    PENDING -> arrow.visibility = View.VISIBLE
                    LOADING -> progressBar.visibility = View.VISIBLE
                    DONE -> check.visibility = View.VISIBLE
                }
                setOnClickListener {
                    click(resource)
                }
            }
    }

}