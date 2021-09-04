package com.zxf.jetpackrelated.lifecycle.service

import androidx.lifecycle.LifecycleService

/**
 * 作者： zxf
 * 描述： lifecycle service demo
 */
class MyLifecycleService : LifecycleService() {

    init {
        val locationServiceLifecycleListener = LocationServiceLifecycleListener()
        lifecycle.addObserver(locationServiceLifecycleListener)
    }
}