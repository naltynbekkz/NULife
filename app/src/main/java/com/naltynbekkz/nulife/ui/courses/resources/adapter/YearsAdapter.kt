package com.naltynbekkz.nulife.ui.courses.resources.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.R
import com.naltynbekkz.nulife.model.Resource
import com.naltynbekkz.nulife.util.Convert
import kotlinx.android.synthetic.main.item_year.view.*

class YearsAdapter(
    private val click: (Resource) -> Unit, private val download: (Resource) -> Unit
) : RecyclerView.Adapter<YearsAdapter.ItemViewHolder>() {

    private var years: ArrayList<Map.Entry<Long, ArrayList<Resource>>> = ArrayList()
    private var offlineData: List<Resource> = ArrayList()

    fun setData(resources: ArrayList<Resource>) {
        this.years = Convert.resourcesToYears(resources)
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
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_year,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(years[position], click, download)

    override fun getItemCount() = years.size

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(
            item: Map.Entry<Long, ArrayList<Resource>>,
            click: (Resource) -> Unit,
            download: (Resource) -> Unit
        ) =
            with(itemView) {
                resourcesRecyclerView.adapter =
                    ResourcesAdapter(
                        item.value,
                        click,
                        download
                    )
                year.text = item.key.toString()
            }
    }

}