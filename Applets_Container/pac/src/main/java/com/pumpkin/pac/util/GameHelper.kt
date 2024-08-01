package com.pumpkin.pac.util

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.pumpkin.data.AppUtil
import com.pumpkin.data.provider.IGParameter
import com.pumpkin.data.provider.IGame
import com.pumpkin.data.provider.LocalGameProvider
import com.pumpkin.data.util.IGameHelper
import com.pumpkin.game.NativeEntrance
import com.pumpkin.game.NativeInfo
import com.pumpkin.pac.BuildConfig
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.view.GameActivity

object GameHelper : IGameHelper {

    fun openGame(context: Context, game: IGame, gp: GParameter = GParameter(notShowLoading = false)) {
        if (game is GameEntity) {
            openGame(context, game, gp)
        } else if (game is NativeInfo) {
            openGame(context, game.who)
        }
    }

    fun openGame(context: Context, gameEntity: GameEntity, gp: GParameter = GParameter(notShowLoading = false)) {
        GameActivity.go(context, gameEntity, gp)
    }

    fun openGame(context: Context, who: Int) {
        NativeEntrance.open(context, who)
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

   override fun starrRandomNextGame(context: Context) {
        LocalGameProvider.randomGetOne()?.let {
            openGame(context, it)
        }

    }

}