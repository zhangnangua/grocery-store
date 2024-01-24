package com.pumpkin.applets_container.data.source

import com.pumpkin.applets_container.data.source.bean.Entity

interface ISource {

    fun key(): String

    fun request(sort: Int, category: String?, search: String?, page: Int = 0, pageSize: Int = 20): List<Entity>

    companion object {
        const val GX_SOURCE = "gx"

        const val SORT_TOP_GAME = 1
        const val SORT_MOST_PLAYED = 2
    }
}