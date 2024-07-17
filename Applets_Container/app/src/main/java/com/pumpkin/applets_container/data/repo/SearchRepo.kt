package com.pumpkin.applets_container.data.repo

import android.util.Log
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.data.source.Entrance
import com.pumpkin.applets_container.view.vh.EditorPickHorizontalVH
import com.pumpkin.applets_container.view.vh.SearchItemVH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.ParseContext
import com.pumpkin.pac.bean.WordCardStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SearchRepo {

    private val entrance = Entrance()

    fun search(flow: MutableStateFlow<List<AdapterWrapBean>>, scope: CoroutineScope, search: String?) {
        entrance.request(scope = scope,
            category = null,
            search = search,
            page = 0,
            pageSize = 20
        ) {data ->
            //  数据打印
            if (AppUtil.isDebug) {
                Log.d(TAG, "superRequest request is ${data.size} , current thread name is ${Thread.currentThread().name} , data is $data")
            }

            if (data.isEmpty()) {
                return@request
            }

            val result = mutableListOf<AdapterWrapBean>()
            data.forEach { entity ->
                result.add(AdapterWrapBean(SearchItemVH.TYPE, entity.toGameEntity()))
            }

            flow.emit(result)
        }
    }

    companion object {
        const val TAG = "SearchRepo"
    }

}