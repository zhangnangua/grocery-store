package com.zxf.jetpackrelated.lifecycle.service

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.zxf.utils.LogUtil

/**
 * 作者： zxf
 * 描述： 实现LifecycleObserver,用于实现注册service的生命周期监听
 */
class LocationServiceLifecycleListener : LifecycleEventObserver {

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        LogUtil.i("service event : $event")
    }
}