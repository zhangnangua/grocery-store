package com.pumpkin.game

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.howie.snake.Snake
import com.pumpkin.data.AppUtil
import dragosholban.com.androidpuzzlegame.MainActivity

object NativeEntrance {
    const val PUZZLE = 1
    const val SNAKE = 2

    fun open(context: Context, who: Int) {
        if (who == PUZZLE) {
            nativeGo(context, Intent(context, MainActivity::class.java))
        } else if (who == SNAKE) {
            nativeGo(context, Intent(context, Snake::class.java))
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