package com.pumpkin.pac.parseStrategy

import android.net.Uri
import android.os.Parcelable
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.thread.IoScope
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.gameIdByUrl
import com.pumpkin.pac.bean.tableToEntity
import com.pumpkin.pac_core.webview.PACWebEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * According the different injected strategies , the corresponding game-related information parsed.
 */
class ParseContext(private val parsed: IParsed, private val engine: PACWebEngine) {
    fun parse(url: Uri?, callback: ((entity: GameEntity?) -> Unit)) {
        IoScope().launch {
            //query from database
            val table = DbHelper.providesGameDao(AppUtil.application).obtainGameById(gameIdByUrl(url?.toString()
                ?: ""))
            if (table != null) {
                callback.invoke(table.tableToEntity())
                return@launch
            }
            //parsed
            withContext(Dispatchers.Main) {
                parsed.parsed(engine, url, callback)
            }
        }
    }
}

abstract class InjectJsParsed : IParsed {

    private val gson = Gson()

    var myParse: ((map: Map<String, String>) -> GameEntity)? = null
    override fun parsed(engine: PACWebEngine, url: Uri?, callback: (entity: GameEntity?) -> Unit) {
        val path = url?.path
        if (url == null || path == null || checkIllegal(url, path)) {
            callback.invoke(null)
            return
        }
        engine.evaluateJavascript(js(url, path)) { str ->

            if (AppUtil.isDebug) {
                Log.d(IParsed.TAG, "parsed () -> $str ")
            }

            val map: Map<String, String>? = try {
                gson.fromJson(str, object : TypeToken<Map<String, String>>() {}.type)
            } catch (e: Exception) {
                null
            }
            if (map == null) {
                callback.invoke(null)
                return@evaluateJavascript
            }
            val parse = myParse
            val entity = if (parse == null) {
                val name = map["name"] ?: ""
                val icon = map["image"] ?: ""
                val link = url.toString()
                GameEntity(id = gameIdByUrl(link), name = name, link = link, icon = icon, tag = null)
            } else {
                parse.invoke(map)
            }
            callback.invoke(entity)
        }
    }

    abstract fun checkIllegal(url: Uri, path: String): Boolean
    abstract fun js(url: Uri, path: String): String
}

interface IParsed : Parcelable {
    fun parsed(engine: PACWebEngine, url: Uri?, callback: ((entity: GameEntity?) -> Unit))

    companion object {
        const val TAG = "IParsed"
    }
}


