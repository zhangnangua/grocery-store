package com.pumpkin.automatic_execution.util

import com.pumpkin.automatic_execution.AppApplication

/**
 * 作者： pumpkin
 * 描述： appUtil
 */
object AppUtil {

    /**
     * obtain application
     */
    @JvmStatic
    lateinit var application: AppApplication
        private set

    /**
     * 是否是debug模式
     */
    var isDebug = true
        private set

    fun inject(appApplication: AppApplication, isDebug: Boolean) {
        this.application = appApplication
        this.isDebug = isDebug
    }


}