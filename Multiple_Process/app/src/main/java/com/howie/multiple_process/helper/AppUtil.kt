package com.howie.multiple_process.helper

import com.howie.multiple_process.App

/**
 * 作者： howie
 * 描述： appUtil
 */
object AppUtil {

    /**
     * obtain application
     */
    @JvmStatic
    lateinit var application: App
        private set

    /**
     * 是否是debug模式
     */
    var isDebug = true
        private set

    fun inject(appApplication: App, isDebug: Boolean) {
        this.application = appApplication
        this.isDebug = isDebug
    }


}