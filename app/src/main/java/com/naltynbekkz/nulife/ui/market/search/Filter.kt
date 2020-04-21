package com.naltynbekkz.nulife.ui.market.search

class Filter(
    var female: Boolean = false,
    var sell: Boolean? = null,
    var category: String? = null,
    var sort: Int = LAST_ACTIVE
){
    companion object{
        val LAST_ACTIVE = 0
        val LOW_FIRST = 1
        val HIGH_FIRST = 2
    }
}