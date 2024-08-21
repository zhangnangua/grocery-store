package com.pumpkin.applets_container.data.repo

import com.pumpkin.applets_container.R
import com.pumpkin.applets_container.bean.SettingEntity
import com.pumpkin.applets_container.view.vh.EditorPickItemVH
import com.pumpkin.applets_container.view.vh.MineNavigationVH
import com.pumpkin.applets_container.view.vh.MineSettingItemVH
import com.pumpkin.applets_container.view.vh.RecentHorizontalVH
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.util.RecentlyNoticeHelper
import com.pumpkin.ui.util.dpToPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class MineRepo {

    fun requestFeed(feedFlow: MutableSharedFlow<List<AdapterWrapBean>>, scope: CoroutineScope) {
        scope.launch(Dispatchers.Default) {
            val data = mutableListOf<AdapterWrapBean>()
            //navigation
            data.add(AdapterWrapBean(MineNavigationVH.TYPE, Any()))

            // recently data
            val recentlyGame = mutableListOf<AdapterWrapBean>()
            RecentlyNoticeHelper.trigger()
            data.add(AdapterWrapBean(RecentHorizontalVH.TYPE, recentlyGame))

            // setting
            data.add(AdapterWrapBean(MineSettingItemVH.TYPE, SettingEntity(title = R.string.privacy_policy, icon = R.drawable.baseline_local_florist_24, marginTop = 20F.dpToPx)))
            data.add(AdapterWrapBean(MineSettingItemVH.TYPE, SettingEntity(R.string.terms_of_service, R.drawable.baseline_flutter_dash_24)))
            data.add(AdapterWrapBean(MineSettingItemVH.TYPE, SettingEntity(R.string.version_info, R.drawable.baseline_emoji_emotions_24)))
            data.add(AdapterWrapBean(MineSettingItemVH.TYPE, SettingEntity(R.string.feed_back, R.drawable.outline_rss_feed_24)))

            feedFlow.emit(data)
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
    }

}