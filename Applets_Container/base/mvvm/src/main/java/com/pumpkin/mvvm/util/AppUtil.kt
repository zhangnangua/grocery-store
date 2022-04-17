package com.pumpkin.mvvm.util

import android.app.Application

/**
 * 作者： zxf
 * 描述： appUtil
 */
object AppUtil {

    /**
     * obtain application
     */
    lateinit var application: Application
        private set

    /**
     * 是否是debug模式
     */
    var isDebug = true
        private set

    fun inject(appApplication: Application, isDebug: Boolean) {
        this.application = appApplication
        this.isDebug = isDebug
    }


}