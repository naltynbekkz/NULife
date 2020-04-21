package com.naltynbekkz.nulife.ui.market.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naltynbekkz.nulife.databinding.ItemMarketBinding
import com.naltynbekkz.nulife.model.Item
import com.naltynbekkz.nulife.ui.market.search.Filter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MarketSearchAdapter(
    private val click: (Item) -> Unit,
    private val like: (Item) -> Unit,
    private val dislike: (String) -> Unit
) : RecyclerView.Adapter<MarketSearchAdapter.ItemViewHolder>() {

    var filter = Filter()

    private var query = ""

    private var items: ArrayList<Item> = ArrayList()
    private var favoriteItems: HashMap<String, Item> = HashMap()

    private var refreshed: ArrayList<Item> = ArrayList()

    private fun refresh() {
        refreshed = ArrayList<Item>().apply {
            items.forEach {
                if (it.fits(filter)
                    && (it.title.toLowerCase(Locale.ROOT).contains(query.toLowerCase().trim())
                            || it.details.toLowerCase().contains(query.toLowerCase().trim()))
                ) {
                    add(it)
                }
            }
        }
        refreshed.sortWith(compareBy {
            if (filter.sort == Filter.LAST_ACTIVE) {
                it.lastActive
            } else {
                it.discountedPrice
            }
        })
        if (filter.sort != Filter.LOW_FIRST) {
            refreshed.reverse()
        }
        notifyDataSetChanged()
    }

    fun setData(items: ArrayList<Item>) {
        this.items = items
        refresh()
    }

    fun setFavoriteData(favoriteItems: List<Item>) {
        this.favoriteItems.clear()
        favoriteItems.forEach {
            this.favoriteItems[it.id] = it
        }
        refresh()
    }

    fun filter(filter: Filter) {
        this.filter = filter
        refresh()
    }

    fun setQuery(query: String) {
        this.query = query
        refresh()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder =
        ItemViewHolder(
            ItemMarketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(
            refreshed[position],
            click,
            like,
            dislike,
            favoriteItems.contains(items[position].id)
        )

    override fun getItemCount(): Int = refreshed.size

    class ItemViewHolder(val binding: ItemMarketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: Item,
            click: (Item) -> Unit,
            like: (Item) -> Unit,
            dislike: (String) -> Unit,
            favorite: Boolean
        ) {
            binding.item = item
            binding.saved = favorite
            binding.root.setOnClickListener {
                click(item)
            }

            binding.likeLayout.setOnClickListener {
                if (favorite) {
                    dislike(item.id)
                } else {
                    like(item)
                }
            }

        }
    }

}
