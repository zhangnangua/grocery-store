package com.pumpkin.applets_container.data.repo

import android.util.Log
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.data.source.Entrance
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.bean.WordCardStyle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class WordRepo {

    fun requestFeed(feedFlow: MutableSharedFlow<List<AdapterWrapBean>>, scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val result = mutableListOf<AdapterWrapBean>()
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://gx.games/favicon.ico", "GX", "https://gx.games/")))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://www.gamepix.com/favicon.ico", "GamePix", "https://www.gamepix.com/")))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://poki.com/favicon.ico", "PoKi", "https://poki.com/zh")))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://yandex.com/favicon.ico", "Yandex", "https://yandex.com/games")))
            feedFlow.emit(result)
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "requestFeed emit () -> $result .")
            }
        }
    }

    fun superRequest(scope: CoroutineScope, category: String? = null, search: String? = null, page: Int = 0, pageSize: Int = 20) {
        Entrance().request(scope = scope,
            category = category,
            search = search,
            pageSize = pageSize,
            page = page) {
            // TODO: 数据打印
            Log.d(TAG, "superRequest request is ${it.size} , current thread name is ${Thread.currentThread().name}")
        }
    }

    companion object {
        const val TAG = "WordRepo"
    }

}