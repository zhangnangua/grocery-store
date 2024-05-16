package com.pumpkin.pac.internal

import android.text.TextUtils
import com.google.gson.Gson
import com.pumpkin.data.AppUtil
import com.pumpkin.data.thread.IoScope
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.responseToEntity
import com.pumpkin.webCache.interceptors.FileUtil
import com.pumpkin.webCache.requestHelper.CacheHelper
import kotlinx.coroutines.launch

object InternalManager {

    private const val CONFIG_JSON_ASSERTS_PATH = "internal_game/config.json"
    private const val CONFIG_ZIP_ASSERTS_PATH = "internal_game/packet/";
    private const val CONFIG__GAME_INFO_JSON_ASSERTS_PATH = "internal_game_info/config.json"

    @Volatile
    private var games: Set<GameEntity>? = null

    @Volatile
    private var gameInfo: Set<GameEntity>? = null

    fun copy() {
        IoScope().launch {
            val game = jsonFileToGame()
            copy(game)
        }
    }

    /**
     * 只返回已解压缩成功的数据
     */
    fun getGames() = jsonFileToGame()


    /**
     * 只返回内置的游戏信息
     */
    fun getGamesInfo() = jsonFileToGameInfo()

    private fun copy(game: Set<GameEntity>) {
        for (entity in game) {
            val id = entity.id
            if (TextUtils.isEmpty(id)) {
                continue
            }
            val link = entity.link
            val outPath = CacheHelper.outPath(AppUtil.application, link) ?: continue
            if (CacheHelper.isUnzipSuccess(outPath, id)) {
                continue
            }
            val result = FileUtil.assetsToUnzip(AppUtil.application.resources, "$CONFIG_ZIP_ASSERTS_PATH$id.zip", outPath)
            if (result) {
                CacheHelper.unzipSuccess(outPath, id)
            }
        }
    }

    private fun jsonFileToGame(): HashSet<GameEntity> {
        val localGames: Set<GameEntity>? = games
        if (localGames != null) {
            return HashSet(localGames)
        }
        synchronized(this) {
            var local = games
            if (local != null) {
                return HashSet(local)
            }
            val parse = Helper.strToListGame(Helper.readJson(CONFIG_JSON_ASSERTS_PATH), Gson())
            local = parse.fold(HashSet()) { acc, response ->
                if (response != null) {
                    acc.add(response.responseToEntity())
                }
                acc
            }
            games = local
            return HashSet(local)
        }
    }

    private fun jsonFileToGameInfo(): HashSet<GameEntity> {
        val localGames: Set<GameEntity>? = gameInfo
        if (localGames != null) {
            return HashSet(localGames)
        }
        synchronized(this) {
            var local = gameInfo
            if (local != null) {
                return HashSet(local)
            }
            val parse = Helper.strToListGame(Helper.readJson(CONFIG__GAME_INFO_JSON_ASSERTS_PATH), Gson())
            local = parse.fold(HashSet()) { acc, response ->
                if (response != null) {
                    acc.add(response.responseToEntity())
                }
                acc
            }
            gameInfo = local
            return HashSet(local)
        }
    }

}