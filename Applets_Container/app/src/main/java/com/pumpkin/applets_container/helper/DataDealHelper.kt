package com.pumpkin.applets_container.helper

import android.util.Log
import com.google.gson.Gson
import com.pumpkin.applets_container.data.internal.Helper
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.pac.bean.entityToTables
import com.pumpkin.pac.bean.entityToTablesMd5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object DataDealHelper {
    const val TAG = "DataDealHelper"

    fun deal() {
        GlobalScope.launch(Dispatchers.IO) {
            Log.d(TAG, "deal () -> start")
            val providesGameDao = DbHelper.providesGameDao(AppUtil.application)

            val saveData = Helper.strToListGame(Helper.readJson("save_life_data.json"), Gson())
                .entityToTablesMd5(GameTable.MODULE_FLOW)
            providesGameDao.insertOrReplaceGameList(saveData)

            Log.d(TAG, "deal () -> end save data . ")

            val rankHot = Helper.strToListGame(Helper.readJson("rank_data_hottest.json"), Gson())
                .entityToTablesMd5(GameTable.MODULE_RANK_HOT)
            providesGameDao.insertOrReplaceGameList(rankHot)

            Log.d(TAG, "deal () -> end rank hottest . ")

            val rankNew = Helper.strToListGame(Helper.readJson("rank_data_new.json"), Gson())
                .entityToTablesMd5(GameTable.MODULE_RANK_NEW)
            providesGameDao.insertOrReplaceGameList(rankNew)

            Log.d(TAG, "deal () -> end rank new . ")
        }


    }


}