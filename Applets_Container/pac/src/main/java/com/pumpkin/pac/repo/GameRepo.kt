package com.pumpkin.pac.repo

import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.RecentlyGameTable
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.entityToTable
import com.pumpkin.pac.bean.entityToTableMd5
import com.pumpkin.pac.util.RecentlyNoticeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GameRepo(val gameEntity: GameEntity, val gParameter: GParameter?) {
    suspend fun recordToRecently() {
        DbHelper.providesGameDao(AppUtil.application).insertOrIgnore(gameEntity.entityToTable(gParameter?.module?: ""))
        withContext(Dispatchers.IO) {
            DbHelper.providesRecentlyGameDao(AppUtil.application)
                .insert(RecentlyGameTable(gameEntity.id, System.currentTimeMillis()))
            RecentlyNoticeHelper.trigger()
        }
    }

}