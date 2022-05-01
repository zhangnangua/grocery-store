package com.pumpkin.mvvm.util

import android.app.Application

/**
 * 作者： pumpkin
 * 描述： appUtil 需要在最外层进行相关初始化操作
 */
object AppUtil {

    /**
     * obtain application
     */
    lateinit var application: Application
        private set

    /**
     * 是否是debug模式  在最外层工程中设置
     */
    var isDebug = true
        private set

    /**
     * 在最外层初始化界面的onCreate中设置为false。
     */
    var isKill = true

    fun inject(appApplication: Application, isDebug: Boolean) {
        this.application = appApplication
        this.isDebug = isDebug
    }


}