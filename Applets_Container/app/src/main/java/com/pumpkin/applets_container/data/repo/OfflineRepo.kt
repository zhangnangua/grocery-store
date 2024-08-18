package com.pumpkin.applets_container.data.repo

import com.pumpkin.applets_container.bean.OfflineInfo
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.view.vh.OfflineBigCardStyle1VH
import com.pumpkin.applets_container.view.vh.OfflineCardStyle1VH
import com.pumpkin.applets_container.view.vh.TitleVH
import com.pumpkin.data.thread.IoScope
import com.pumpkin.game.NativeEntrance
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.internal.InternalManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class OfflineRepo {

    fun request(feedFlow: MutableStateFlow<List<AdapterWrapBean>>) {
        IoScope().launch {
            val result = mutableListOf<AdapterWrapBean>()

            //native games
//            result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.PUZZLE))))
            result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.BLOCKS))))
            result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.SUPER_MAIN))))

            //H5 Games
            for (game in InternalManager.getGames()) {
                result.add(AdapterWrapBean(OfflineCardStyle1VH.TYPE, OfflineInfo(gameEntity = game, isInternal = true)))
            }
            result.shuffle()

            //头图
            result.add(0,AdapterWrapBean(OfflineBigCardStyle1VH.TYPE, OfflineInfo(isNative = true, nativeInfo = NativeEntrance.obtainInfo(NativeEntrance.PIXEL_DUNGEON))))
            //title
            result.add(0, AdapterWrapBean(TitleVH.TYPE, TitleEntity("Built-in Games", "Always available, even offline.")))

            feedFlow.emit(result)
        }
    }
}