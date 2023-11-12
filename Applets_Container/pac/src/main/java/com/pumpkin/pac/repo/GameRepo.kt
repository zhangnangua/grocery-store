package com.pumpkin.pac.repo

import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.RecentlyGameTable
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.util.RecentlyNoticeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepo(val gameEntity: GameEntity) {
    suspend fun recordToRecently() {
        withContext(Dispatchers.IO) {
            DbHelper.providesRecentlyGameDao(AppUtil.application)
                .insert(RecentlyGameTable(gameEntity.id, System.currentTimeMillis()))
            RecentlyNoticeHelper.trigger()
        }
    }

}