package com.pumpkin.pac.util

import android.content.Context
import android.content.Intent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.BuildConfig
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.view.GameActivity

object GameHelper {

    fun openGame(context: Context, gameEntity: GameEntity, gp: GParameter = GParameter(notShowLoading = false)) {
        GameActivity.go(context, gameEntity, gp)
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

    fun starrRandomNextGame(context: Context) {
        // TODO: 数据源
        val games = InternalManager.getGames()
        openGame(context, games.random())
    }

}