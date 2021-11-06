package com.zxf.jetpackrelated.ServiceTest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import kotlin.random.Random

class BindServiceSimpleDemo : Service() {

    /**
     * 重写 返回bind
     */
    override fun onBind(intent: Intent?): IBinder = MyBinder()

    /**
     * 对外提供的方法
     */
    fun getRandomNumber() = Random(System.currentTimeMillis())

    inner class MyBinder : Binder() {
        /**
         * 提供方法返回 service  实例
         */
        fun getService() = this@BindServiceSimpleDemo
    }
}