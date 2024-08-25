package com.pumpkin.applets_container.data.repo

import android.util.Log
import com.pumpkin.applets_container.BuildConfig
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.view.vh.EditorPickHorizontalVH
import com.pumpkin.applets_container.view.vh.EditorPickItemVH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.applets_container.view.vh.WordCardStyle1VH
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
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "gamesnack.png", "GameSnacks", "https://gamesnacks.com/", ParseContext.GAME_SNACK)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "poki.png", "PoKi", "https://poki.com", ParseContext.POKI)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "gamepix.png", "GamePix", "https://www.gamepix.com/", ParseContext.GAME_PIX)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "crazygames.jpeg", "CrazyGames", "https://www.crazygames.com/", ParseContext.CRAZY_GAMES)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "gx.jpeg", "GX", "https://gx.games/",ParseContext.GX)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "h5games.png", "Html5games", "https://html5games.com/", ParseContext.HTML5_GAMES)))
            result.add(AdapterWrapBean(WordCardStyle1VH.TYPE, WordCardStyle(System.currentTimeMillis(), ICON_PREFIX + "famobi.jpeg", "Famobi", "https://famobi.com/", ParseContext.FAMOBI)))

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

    companion object {
        const val TAG = "HomeRepo"

        const val ICON_PREFIX = "file:///android_asset/browser/icon/"
    }

}