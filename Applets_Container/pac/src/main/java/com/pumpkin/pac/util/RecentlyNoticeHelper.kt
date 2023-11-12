package com.pumpkin.pac.util

import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.mvvm.util.Event
import com.pumpkin.mvvm.util.EventHelper
import com.pumpkin.mvvm.util.ProcessEventBus

object RecentlyNoticeHelper :
    ProcessEventBus<RecentlyNoticeHelper.RecentlyEvent>(
        EventHelper.TYPE_RECENTLY,
        AppUtil.application,
        null,
        "recently_games") {

    private const val RECENTLY_NUM = 10

    fun recentItemFlow() = getSharedFlow()

    override fun searchData(): RecentlyEvent {
        return RecentlyEvent(DbHelper.providesRecentlyGameDao(AppUtil.application).obtainGame(RECENTLY_NUM))
    }

    class RecentlyEvent(val games: List<GameTable>) : Event
}