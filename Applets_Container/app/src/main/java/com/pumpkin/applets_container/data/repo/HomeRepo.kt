package com.pumpkin.applets_container.data.repo

import android.text.TextUtils
import android.util.Log
import androidx.paging.PagingConfig
import com.pumpkin.applets_container.bean.TitleEntity
import com.pumpkin.applets_container.view.vh.*
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.mvvm.adapter.AdapterWrapBean
import com.pumpkin.mvvm.repo.BasePageRepo
import com.pumpkin.pac.bean.tablesToEntities
import kotlinx.coroutines.delay

object HomeRepo : BasePageRepo(BigCardPagingSource()) {
    private const val BIG_CARD_NUM = 10
    const val TAG = "HomeRepo"

    private const val RECENTLY_NUM = 10

    fun getBigCardPagingData() = getPagingData(
        PagingConfig(
            pageSize = BIG_CARD_NUM,
            prefetchDistance = BIG_CARD_NUM,
            initialLoadSize = BIG_CARD_NUM * 2
        )
    )

    class BigCardPagingSource : BasePagingSource() {

        private val excludeIds = mutableMapOf<Int, List<String>>()

        override suspend fun hasNext(
            data: List<AdapterWrapBean>,
            page: Int,
            pageSize: Int,
            prevKey: Int?
        ): Boolean = data.isNotEmpty()

        override suspend fun handlerData(
            page: Int,
            pageSize: Int,
            prevKey: Int?
        ): List<AdapterWrapBean> {
            val gameDao = DbHelper.providesGameDao(AppUtil.application)
            val recentlyGameDao = DbHelper.providesRecentlyGameDao(AppUtil.application)
            //数据库查询数据
            val gameEntities = if (page == 1) {
                excludeIds.clear()
                gameDao.obtainGameByRandom(pageSize)
                    .tablesToEntities()
            } else {
                val ids = arrayListOf<String>()
                if (prevKey != null) {
                    for (i in 1..prevKey) {
                        ids.addAll(excludeIds[i] ?: emptyList())
                    }
                }
                gameDao
                    .obtainGameByRandomExclude(pageSize, ids)
                    .tablesToEntities()
            }
            // recently data
            val recentlyGame = mutableListOf<AdapterWrapBean>()
            if (page == 1) {
                for (entity in recentlyGameDao.obtainGame(RECENTLY_NUM).tablesToEntities()) {
                    recentlyGame.add(AdapterWrapBean(RecentItemVH.TYPE, entity))
                }
            }

            // TODO: 模拟延时处理
            delay(2000)

            if (AppUtil.isDebug) {
                Log.d(TAG, "load () -> excludeIds is $excludeIds , data is $gameEntities .")
            }

            //数据处理
            //处理好的数据
            val data = mutableListOf<AdapterWrapBean>()
            //当前需要排除掉的id
            val ids = arrayListOf<String>()

            //如果是首次 则组装  carousel数据
            if (page == 1) {
                val carouselData = mutableListOf<AdapterWrapBean>()
                val size = if (gameEntities.size > 10) {
                    10
                } else {
                    gameEntities.size - 1
                }

                for (i in 0 until size) {
                    val it = gameEntities.removeAt(0)
                    val id = it.id
                    if (!TextUtils.isEmpty(id)) {
                        ids.add(id)
                        carouselData.add(AdapterWrapBean(CarouselItemVH.TYPE, it))
                    }
                }
                if (recentlyGame.isNotEmpty()) {
                    data.add(AdapterWrapBean(RecentHorizontalVH.TYPE, recentlyGame))
                }
                if (carouselData.isNotEmpty()) {
                    data.add(AdapterWrapBean(CarouselHorizontalVH.TYPE, carouselData))
                }
            }

            //big card 数据
            if (gameEntities.isNotEmpty() && page == 1) {
                data.add(AdapterWrapBean(TitleVH.TYPE, TitleEntity("Trending Games")))
            }
            gameEntities.forEach {
                val id = it.id
                if (!TextUtils.isEmpty(id)) {
                    ids.add(id)
                    data.add(AdapterWrapBean(BigCardVH.TYPE, it))
                    excludeIds[page] = ids
                }
            }
            return data
        }

    }
}