package com.pumpkin.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.howie.snake.SnakeActivity
import com.pumpkin.data.AppUtil
import com.pumpkin.mvvm.setting_bean.ActivitySettingBean
import com.pumpkin.mvvm.util.Constant
import dragosholban.com.androidpuzzlegame.MainActivity

object NativeEntrance {
    const val PUZZLE = 1
    const val SNAKE = 2

    fun open(context: Context, who: Int) {
        if (who == PUZZLE) {
            nativeGo(context, Intent(context, MainActivity::class.java))
        } else if (who == SNAKE) {
            nativeGo(context, Intent(context, SnakeActivity::class.java).apply {
                putExtra(Constant.PAGE_PARAMETER, ActivitySettingBean().apply {
                    enableImmersiveBar = true
                })
            })
        }
    }

    fun obtainInfo(who: Int) = DataProvider.obtainInfo(who)

    private fun nativeGo(context: Context, intent: Intent) {
        if (context is Activity) {
            context.startActivity(intent)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            AppUtil.application.startActivity(intent)
        }
    }
}