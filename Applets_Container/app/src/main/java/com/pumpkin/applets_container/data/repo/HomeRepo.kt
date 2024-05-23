package com.pumpkin.applets_container.data.repo

import android.util.Log
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.data.source.Entrance
import com.pumpkin.applets_container.view.vh.EditorPickHorizontalVH
import com.pumpkin.applets_container.view.vh.EditorPickItemVH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.ParseContext
import com.pumpkin.pac.bean.WordCardStyle
import com.pumpkin.pac.internal.InternalManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class HomeRepo {

    fun requestFeed(feedFlow: MutableSharedFlow<List<AdapterWrapBean>>, scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val result = mutableListOf<AdapterWrapBean>()
            result.add(AdapterWrapBean(TitleVH.TYPE, TitleEntity("Editor's Picks")))
            result.add(AdapterWrapBean(EditorPickHorizontalVH.TYPE, editorPickData()))

            result.add(AdapterWrapBean(TitleVH.TYPE, TitleEntity("Game Center")))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://ssl.gstatic.com/h5games_gamecenter/images/icon-512x512.png", "GameSnacks", "https://gamesnacks.com/", ParseContext.GAME_SNACK)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://a.poki.com/icons/fav-512.png", "PoKi", "https://poki.com", ParseContext.POKI)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://www.shakagame.com/favicon.ico", "ShakaGame", "https://www.shakagame.com/", ParseContext.SHAKA_GAME)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://html5games.com/favicon.ico", "Html5games", "https://html5games.com/", ParseContext.HTML5_GAMES)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://www.gamepix.com/favicon.ico", "GamePix", "https://www.gamepix.com/", ParseContext.GAME_PIX)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://gx.games/favicon.ico", "GX", "https://gx.games/")))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://www.crazygames.com/favicon.ico", "CrazyGames", "https://www.crazygames.com/", ParseContext.CRAZY_GAMES)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), "https://famobi.com/favicon.ico", "Famobi", "https://famobi.com/", ParseContext.FAMOBI)))

            feedFlow.emit(result)

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "requestFeed emit () -> $result .")
            }
        }
    }

    private fun editorPickData(): List<AdapterWrapBean> {
        val editorPickWrap = mutableListOf<AdapterWrapBean>()
        InternalManager.getGamesInfo().forEach {
            editorPickWrap.add(AdapterWrapBean(EditorPickItemVH.TYPE, it))
        }
        return editorPickWrap
    }

    fun superRequest(scope: CoroutineScope, category: String? = null, search: String? = null, page: Int = 0, pageSize: Int = 20) {
        Entrance().request(scope = scope,
            category = category,
            search = search,
            pageSize = pageSize,
            page = page) {
            // TODO: 数据打印
            if (AppUtil.isDebug) {
                Log.d(TAG, "superRequest request is ${it.size} , current thread name is ${Thread.currentThread().name}")
            }
        }
    }

    companion object {
        const val TAG = "HomeRepo"
    }

}