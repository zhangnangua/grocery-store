package com.pumpkin.game

import android.app.Activity
import android.content.Intent
import dragosholban.com.androidpuzzlegame.MainActivity

class NativeEntrance {
    fun openQuiz(activity: Activity) {
        activity.startActivity(Intent(activity, MainActivity::class.java))
    }
}