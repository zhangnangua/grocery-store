package com.pumpkin.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.pumpkin.data.AppUtil
import com.pumpkin.dgx.AndroidLauncher

object NativeEntrance {
    const val BLOCKS = 1
    const val SUPER_MAIN = 2
    const val PIXEL_DUNGEON = 3

    fun open(context: Context, who: Int) {
        if (who == BLOCKS) {
            nativeGo(context, Intent(context, AndroidLauncher::class.java))
        } else if (who == SUPER_MAIN) {
            nativeGo(context, Intent(context, com.pumpkin.dgx_super_boy.AndroidLauncher::class.java))
        } else if (who == PIXEL_DUNGEON) {
            nativeGo(context, Intent(context, com.pumpkin.dgx_pixel_dungeon.AndroidLauncher::class.java))
        }
    }

    fun obtainInfo(who: Int) = DataProvider.obtainInfo(who)

    fun obtainInfo() = DataProvider.obtainInfo()

}

private fun nativeGo(context: Context, intent: Intent) {
    if (context is Activity) {
        context.startActivity(intent)
    } else {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        AppUtil.application.startActivity(intent)
    }
}