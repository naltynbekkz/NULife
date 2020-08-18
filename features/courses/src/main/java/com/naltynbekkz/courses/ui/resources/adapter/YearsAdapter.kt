package com.naltynbekkz.courses.ui.resources.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.courses.databinding.ItemYearBinding
import com.naltynbekkz.courses.model.Resource
import java.util.*
import kotlin.collections.ArrayList

class YearsAdapter(
    private val click: (Resource) -> Unit, private val download: (Resource) -> Unit
) : RecyclerView.Adapter<YearsAdapter.ItemViewHolder>() {

    private var years: ArrayList<Map.Entry<Long, ArrayList<Resource>>> = ArrayList()
    private var offlineData: List<Resource> = ArrayList()

    fun setData(resources: ArrayList<Resource>) {
        this.years = resourcesToYears(resources)
        mix()
    }

    fun setOfflineData(resources: List<Resource>) {
        this.offlineData = resources
        mix()
    }

    private fun mix() {
        offlineData.forEach { resource ->
            for (pair in years) {
                var found = false
                for (it in pair.value) {
                    if (it.id == resource.id) {
                        it.status = resource.status
                        found = true
                        break
                    }
                }
                if (found) {
                    break
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ItemViewHolder(
            ItemYearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(years[position], click, download)

    override fun getItemCount() = years.size

    class ItemViewHolder(val binding: ItemYearBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Map.Entry<Long, ArrayList<Resource>>,
            click: (Resource) -> Unit,
            download: (Resource) -> Unit
        ) {
            binding.resourcesRecyclerView.adapter =
                ResourcesAdapter(
                    item.value,
                    click,
                    download
                )
            binding.year.text = item.key.toString()
        }
    }

    private fun resourcesToYears(resources: ArrayList<Resource>): ArrayList<Map.Entry<Long, ArrayList<Resource>>> {
        return ArrayList<Map.Entry<Long, ArrayList<Resource>>>(TreeMap<Long, ArrayList<Resource>>().apply {
            for (resource in resources) {
                if (containsKey(resource.year)) {
                    this[resource.year]!!.add(resource)
                } else {
                    this[resource.year] = ArrayList<Resource>().apply {
                        add(resource)
                    }
                }
            }
        }.entries).apply {
            reverse()
        }
    }

}