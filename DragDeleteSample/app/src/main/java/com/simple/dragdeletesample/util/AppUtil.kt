package com.simple.dragdeletesample.util

import android.app.Application

/**
 * 作者： zxf
 * 描述： appUtil
 */
object AppUtil {
    lateinit var application: Application
        private set

    var debug: Boolean = false
        private set

    fun inject(application: Application, debug: Boolean = false) {
        AppUtil.application = application
        AppUtil.debug = debug
    }
}