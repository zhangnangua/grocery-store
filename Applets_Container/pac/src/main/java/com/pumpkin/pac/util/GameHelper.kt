package com.pumpkin.pac.util

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.data.db.entity.RecentlyGameTable
import com.pumpkin.data.provider.IGParameter
import com.pumpkin.data.provider.IGame
import com.pumpkin.data.provider.LocalGameProvider
import com.pumpkin.data.thread.IoScope
import com.pumpkin.data.util.IGameHelper
import com.pumpkin.game.NativeEntrance
import com.pumpkin.game.NativeInfo
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.pac.BuildConfig
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.entityToTable
import com.pumpkin.pac.view.GameActivity
import kotlinx.coroutines.launch

object GameHelper : IGameHelper {

    fun openGame(context: Context, game: IGame) {
        if (game is GameEntity) {
            openGame(context, game, GParameter(notShowLoading = true, module = "", orientation = game.orientation
                ?: Constant.INVALID_ID))
        } else if (game is NativeInfo) {
            openGame(context, game.who)
        }
    }

    fun openGame(context: Context, gameEntity: GameEntity, gp: GParameter = GParameter(notShowLoading = false, orientation = gameEntity.orientation
        ?: Constant.INVALID_ID)) {
        if (gameEntity.type == GameTable.TYPE_NATIVE_GAME) {
            openGame(context, gameEntity.id)
            return
        }
        GameActivity.go(context, gameEntity, gp)
        recordToRecently(gameEntity, gp)
    }

    fun openGame(context: Context, who: String) {
        NativeEntrance.open(context, who)
        try {
            val obtainInfo = NativeEntrance.obtainInfo(who)
            recordToRecently(
                GameEntity(id = who, name = obtainInfo.name, link = "", describe = "", icon = obtainInfo.icon, bigIcon = obtainInfo.icon, extra = null, type = GameTable.TYPE_NATIVE_GAME, tag = ""),
                null)
        } catch (_: Exception) {
        }

    }

    override fun openGame(context: Context, game: IGame, gp: IGParameter) {
        if (gp is GParameter) {
            if (game is GameEntity) {
                openGame(context, game, gp)
            } else if (game is NativeInfo) {
                openGame(context, game.who)
            }
        }
    }


    override fun starrRandomNextGame(context: Context) {
        LocalGameProvider.randomGetOne()?.let {
            openGame(context, it)
        }

    }

    fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?,
        load: (url: String) -> Unit
    ): Boolean {
        if (request != null && request.url != null) {
            val uri = request.url
            val scheme = uri.scheme
            if (scheme?.startsWith("http") == true) {
                load.invoke(uri.toString())
            } else {
                try {
                    AppUtil.application.startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            uri
                        ).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    )
                } catch (e: Exception) {
                    if (BuildConfig.DEBUG) {
                        throw e
                    }
                }
            }
        }
        return true
    }

    private fun recordToRecently(gameEntity: GameEntity, g: GParameter? = null) {
        IoScope().launch {
            DbHelper.providesGameDao(AppUtil.application).insertOrIgnore(gameEntity.entityToTable(g?.module
                ?: ""))
            DbHelper.providesRecentlyGameDao(AppUtil.application).insert(RecentlyGameTable(gameEntity.id, System.currentTimeMillis()))
            RecentlyNoticeHelper.trigger()
        }
    }

}