package com.zxf.jetpackrelated.lifecycle.activityOrfragment

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.zxf.jetpackrelated.lifecycle.mock.MockLocationManager
import com.zxf.jetpackrelated.utils.LogUtil

/**
 * 作者： zxf
 * 描述： 实现LifecycleObserver,用于实现注册activity或者fragment的生命周期监听
 * @param block 获取位置信息回调模拟
 */
class LocationLifecycleListener(private val block: (Pair<Int, Int>) -> Unit) :
    LifecycleEventObserver {

    init {
        //初始化
        MockLocationManager.initLocationManager()
    }

    /**
     * 生命周期状态改变后回调这里
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> {
                //开始获取位置信息
                MockLocationManager.startGetLocation()
            }
            Lifecycle.Event.ON_PAUSE -> {
                //停止获取位置信息
                MockLocationManager.stopGetLocation()
            }
            Lifecycle.Event.ON_DESTROY -> {
                //移除生命周期监听
                source.lifecycle.removeObserver(this)
                LogUtil.i("MockLocationManager ${event.name} lifecycle observer remove success.")
            }
            else -> {
                //其他生命周期监听
                LogUtil.i("MockLocationManager ${event.name}")
            }
        }
    }

}