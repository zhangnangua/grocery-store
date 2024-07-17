package com.pumpkin.applets_container.data.source

import com.pumpkin.applets_container.data.source.bean.Entity
import com.pumpkin.applets_container.data.source.gx.GXSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Entrance {

    private val sources: MutableList<ISource> = mutableListOf()

    init {
        sources.add(GXSource())
    }

    fun request(sort: Int = ISource.SORT_TOP_GAME, scope: CoroutineScope, category: String? = null, search: String? = null, page: Int = 0, pageSize: Int = 20, block: suspend CoroutineScope.(result: List<Entity>) -> Unit) {
        scope.launch {
            val result = ArrayList<Entity>()
            sources.map {
                async {
                    it.request(sort, category, search, page, pageSize)
                }
            }.map {
                it.await()
            }.forEach {
                result.addAll(it)
            }
            block.invoke(this, result)
        }

    }


}