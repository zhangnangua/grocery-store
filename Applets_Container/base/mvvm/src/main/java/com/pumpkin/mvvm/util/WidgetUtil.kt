package com.pumpkin.mvvm.util

import android.app.PendingIntent
import android.content.ClipData
import android.content.ClipboardManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.pumpkin.data.AppUtil


object WidgetUtil {
    fun createShortcut(context: Context = AppUtil.application, icon: String, title: String, id: String, clsName: String) {
        Glide.with(context).asBitmap().load(icon).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(p0: Bitmap, p1: Transition<in Bitmap>?) {
                val comp = ComponentName(context.packageName, clsName)
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setComponent(comp)
                intent.setPackage(context.packageName)
                val shortcutInfoCompat: ShortcutInfoCompat = ShortcutInfoCompat.Builder(context, id).setShortLabel(title).setIcon(IconCompat.createWithBitmap(p0)).setIntent(intent).build()
                val shortCreateIntent = Intent(context.packageName + ".action.SHORTCUT_CREATE")
                shortCreateIntent.setPackage(context.packageName)
                val flags: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
                val pendingIntent = PendingIntent.getBroadcast(context, id.hashCode(), shortCreateIntent, flags)
                try {
                    ShortcutManagerCompat.requestPinShortcut(context, shortcutInfoCompat, pendingIntent.intentSender)
                } catch (ignore: Exception) {
                }
            }

            override fun onLoadCleared(p0: Drawable?) {

            }

        })
    }

    fun copyTextToClipboard(context: Context = AppUtil.application, text: String?) {
        val clipboard: ClipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("label", text)
        clipboard.setPrimaryClip(clip)
    }
}