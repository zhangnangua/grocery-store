package com.pumpkin.pac

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.webkit.WebView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pumpkin.data.AppUtil
import com.pumpkin.data.thread.ThreadHelper
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.gameIdByUrl
import com.pumpkin.pac.internal.Helper
import com.pumpkin.pac.util.GameHelper
import com.pumpkin.pac_core.webview.PACWebEngine


/**
 * According the different injected strategies , the corresponding game-related information parsed.
 */
class ParseContext(private val injectName: String, private val engine: PACWebEngine) {
    private val injector = Injector(injectName)
    private val gson = Gson()
    private var injectRunnable: (() -> Unit)? = null
    fun inject() {
        val delay = if (injectName == FAMOBI) {
            2500L
        } else {
            0L
        }
        val localInjectRunnable = {
            injector.inject(engine)
        }
        injectRunnable = localInjectRunnable
        ThreadHelper.runOnUiThreadDelay(localInjectRunnable, delay)
    }

    fun destroy() {
        val localInjectRunnable = injectRunnable
        if (localInjectRunnable != null) {
            ThreadHelper.removeUiThread(localInjectRunnable)
        }
        injectRunnable = null
    }

    fun callback(context: Context, url: String?, message: String?): Boolean {
        if (AppUtil.isDebug) {
            Log.d(Injector.TAG, "callback () -> map message is $message .")
        }
        val map: Map<String, String> = try {
            val localMap: Map<String, String> = gson.fromJson(message, object : TypeToken<Map<String, String>>() {}.type)
            localMap
        } catch (e: Exception) {
            null
        } ?: return false
        val link = map["link"] ?: url.toString()
        if (TextUtils.isEmpty(link)) {
            return false
        }
        val name = map["name"] ?: ""
        if (TextUtils.isEmpty(name)) {
            return false
        }
        val icon = map["img"] ?: ""
        GameHelper.openGame(context, GameEntity(id = gameIdByUrl(link), name = name, link = link, icon = icon, tag = null))
        return true
    }

    companion object {
        const val CRAZY_GAMES = "crazygames"
        const val GAME_PIX = "gamepix"
        const val GAME_SNACK = "gamesnack"
        const val POKI = "poki"
        const val HTML5_GAMES = "html5games"
        const val FAMOBI = "famobi"
        const val SHAKA_GAME = "shakagame"
    }
}

private class Injector(private val injectName: String) {

    fun inject(engine: PACWebEngine) {
        val jsCode = jsCode()
        if (AppUtil.isDebug) {
            Log.d(TAG, "inject () -> js code is $jsCode")
        }
        if (TextUtils.isEmpty(jsCode)) {
            return
        }

        WebView.setWebContentsDebuggingEnabled(true)

        engine.evaluateJavascript(jsCode!!, null)
        if (AppUtil.isDebug) {
            Log.d(TAG, "inject () -> evaluateJavascript ")
        }
    }

    private fun jsCode(): String? {
        // TODO: 兼容fb的逻辑
        val path = CODE_PATH_PREFIX + injectName + CODE_PATH_SUFFIX
        return Helper.readJson(path)
    }

    companion object {
        const val TAG = "Injector"
        const val CODE_PATH_PREFIX = "browser/"
        const val CODE_PATH_SUFFIX = ".pac"
    }
}


