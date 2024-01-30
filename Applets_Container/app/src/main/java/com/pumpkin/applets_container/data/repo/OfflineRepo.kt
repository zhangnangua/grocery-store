package com.pumpkin.applets_container.data.repo

import com.pumpkin.applets_container.bean.OfflineInfo
import com.pumpkin.applets_container.view.vh.OfflineCardStyle1VH
import com.pumpkin.game.NativeEntrance
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.internal.InternalManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OfflineRepo {

    fun request(feedFlow: MutableStateFlow<List<AdapterWrapBean>>, scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val result = mutableListOf<AdapterWrapBean>()

            //native games
            result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.PUZZLE))))
            result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.SNAKE))))

            //H5 Games
            for (game in InternalManager.getGames()) {
                result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(gameEntity = game, isNative = false, isInternal = true)))
            }

            feedFlow.emit(result)
        }
    }
}