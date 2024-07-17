package com.pumpkin.applets_container.data.source.gx

import android.util.Log
import com.google.gson.Gson
import com.pumpkin.applets_container.data.source.ISource
import com.pumpkin.applets_container.data.source.bean.Entity
import com.pumpkin.applets_container.data.source.bean.GXEntity
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.data.http.OkHttp
import com.pumpkin.data.http.newCall
import com.pumpkin.data.thread.IoScope
import com.pumpkin.data.util.UriUtil
import com.pumpkin.ui.util.FormatUtil
import kotlinx.coroutines.launch
import java.lang.RuntimeException

class GXSource : ISource {

    private val gson = Gson()

    private val isScope = IoScope()

    override fun key(): String = ISource.GX_SOURCE

    override fun request(sort: Int, category: String?, search: String?, page: Int, pageSize: Int): List<Entity> {
        if (AppUtil.isDebug){
            Log.d(TAG, "request () -> ")
        }
        val url = getUrl(sort, category, search, page, pageSize)
        try {
            //request
            val response = request(url)
            //deal
            if (response.isSuccessful) {
                response.body?.string()?.let {
                    val gxEntity = gson.fromJson(it, GXEntity::class.java)
                    val result = mapData(gxEntity)
                    if (result.isNotEmpty()) {
                        //save
                        isScope.launch {
                            DbHelper.providesGameDao(AppUtil.application).insertOrReplaceGameList(result.map { entity ->
                                // TODO: do 数据复用
                                entity.toGameTable(GameTable.MODULE_GX_)
                            }.toList())
                        }

                        return result
                    }
                }
            }
        } catch (e: Exception) {
            if (AppUtil.isDebug){
                throw RuntimeException(e)
            }
        }
        return emptyList()
    }

    private fun request(url: String) = OkHttp.getBuilder()
        .url(url)
        .build()
        .newCall()
        .execute()

    private fun mapData(gxEntity: GXEntity?): List<Entity> {
        val result = ArrayList<Entity>()
        gxEntity?.data?.games?.forEach { game ->

            var entity: Entity? = null

            if (game?.gameId != null) {
                val icon = if (game.covers != null && game.covers!!.isNotEmpty()) {
                    game.covers!![0]?.coverUrl
                } else if (game.graphics != null && game.graphics!!.isNotEmpty()) {
                    game.graphics!![0]
                } else {
                    null
                }

                val tags = StringBuilder()
                game.tags?.forEach { tag ->
                    if (tag?.title != null) {
                        tags.append(tag.title!!)
                        tags.append(";")
                    }
                }

                entity = Entity(key() + game.gameId,
                    icon ?: "",
                    BASE_LINK + game.gameShortId,
                    game.title ?: "",
                    tags.toString(),
                    game.shortDescription ?: ""
                )

            }
            entity?.let {
                result.add(it)
            }
        }
        return result
    }

    private fun getUrl(sort: Int, category: String?, search: String?, page: Int, pageSize: Int): String {
        return UriUtil.urlToUri(BASE_URL).buildUpon().also {
            if (sort == ISource.SORT_TOP_GAME) {
                it.appendQueryParameter("sort", "total-time-played-desc")
            } else if (sort == ISource.SORT_MOST_PLAYED) {
                it.appendQueryParameter("sort", "total-plays-desc")
            }
            if (search != null) {
                it.appendQueryParameter("search", search)
            }
            it.appendQueryParameter("pageSize", pageSize.toString())
            it.appendQueryParameter("platform", "MOBILE")
            if (category != null) {
                it.appendQueryParameter("tagAlias", category)
            }
            it.appendQueryParameter("startDate", FormatUtil.timestampToDate(System.currentTimeMillis()))
            it.appendQueryParameter("page", page.toString())
        }.build().toString()
    }

    private

    companion object {
        const val TAG = "GXSource"
        const val BASE_URL = "https://api.gx.games/gxc/v3/games"

        const val BASE_LINK = "https://gx.games/games/"
    }
}