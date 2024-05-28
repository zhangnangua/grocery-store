package com.pumpkin.pac.repo

import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.gson.Gson
import com.pumpkin.data.AppUtil
import com.pumpkin.data.db.DbHelper
import com.pumpkin.data.db.entity.RecentlyGameTable
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.pac.bean.GParameter
import com.pumpkin.pac.bean.GameEntity
import com.pumpkin.pac.bean.entityToTable
import com.pumpkin.pac.util.RecentlyNoticeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class GameRepo(val gameEntity: GameEntity, val gParameter: GParameter?) {
    suspend fun recordToRecently() {
        DbHelper.providesGameDao(AppUtil.application).insertOrIgnore(gameEntity.entityToTable(gParameter?.module
            ?: ""))
        withContext(Dispatchers.IO) {
            DbHelper.providesRecentlyGameDao(AppUtil.application).insert(RecentlyGameTable(gameEntity.id, System.currentTimeMillis()))
            RecentlyNoticeHelper.trigger()
        }
    }

    fun createShortcut(context: Context = AppUtil.application) {
        Glide.with(context).asBitmap().load(gameEntity.icon).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(p0: Bitmap, p1: Transition<in Bitmap>?) {
                val comp = ComponentName(context.packageName, "com.pumpkin.pac.view.GameActivity")
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setComponent(comp)
                intent.setPackage(context.packageName)
                shortCutParseToIntent(intent, gameEntity, gParameter)
                val shortcutInfoCompat: ShortcutInfoCompat = ShortcutInfoCompat.Builder(context, gameEntity.id).setShortLabel(gameEntity.name).setIcon(IconCompat.createWithBitmap(p0)).setIntent(intent).build()
                val shortCreateIntent = Intent(context.packageName + ".action.SHORTCUT_CREATE")
                shortCreateIntent.setPackage(context.packageName)
                val flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
                val pendingIntent = PendingIntent.getBroadcast(context, gameEntity.id.hashCode(), shortCreateIntent, flags)
                try {
                    ShortcutManagerCompat.requestPinShortcut(context, shortcutInfoCompat, pendingIntent.intentSender)
                } catch (ignore: Exception) {
                }
            }

            override fun onLoadCleared(p0: Drawable?) {

            }

        })
    }


    companion object {
        const val TAG = "GameRepo"
        const val SHORT_CUT_PARAMETER = "short_"

        fun shortCutParseToIntent(intent: Intent, gameEntity: GameEntity, gParameter: GParameter?, gson: Gson = Gson()) {
            intent.putExtra(SHORT_CUT_PARAMETER + Constant.FIRST_PARAMETER, gson.toJson(gameEntity))
            if (gParameter != null) {
                intent.putExtra(SHORT_CUT_PARAMETER + Constant.SECOND_PARAMETER, gson.toJson(gParameter))
            }
        }

        fun shortCurParseGetEntity(bundle: Bundle, gson: Gson): GameEntity? {
            return try {
                gson.fromJson(bundle.getString(SHORT_CUT_PARAMETER + Constant.FIRST_PARAMETER), GameEntity::class.java)
            } catch (e: Exception) {
                null
            }
        }

        fun shortCurParseGetParameter(bundle: Bundle, gson: Gson): GParameter? {
            return try {
                gson.fromJson(bundle.getString(SHORT_CUT_PARAMETER + Constant.SECOND_PARAMETER), GParameter::class.java)
            } catch (e: Exception) {
                null
            }
        }
    }

}