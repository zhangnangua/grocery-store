package com.zxf.jetpackrelated.lifecycle.application

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.zxf.jetpackrelated.utils.LogUtil

/**
 * 作者： zxf
 * 描述： process lifecycle observer
 */

typealias processEvent = () -> Unit

class ProcessLifecycleObserver : LifecycleEventObserver {

    /**
     * process 上一个生命周期事件
     */
    var lastEvent = Lifecycle.Event.ON_ANY

    /**
     * app启动
     */
    var appStart: processEvent? = null
        private set

    /**
     * app回到前台
     */
    private var appForeground: processEvent? = null
        private set

    /**
     * app回到后台
     */
    private var appBackground: processEvent? = null
        private set

    /**
     * 注册APP 启动  回到前台  退到后台 事件
     */
    fun inject(
        appStart: processEvent? = null,
        appForeground: processEvent? = null,
        appBackground: processEvent? = null
    ) {
        this.appStart = appStart
        this.appForeground = appForeground
        this.appBackground = appBackground
    }

    /**
     * process event  说明   调用顺序如下所示
     *
     * onCreate 在应用程序整个生命周期中只调用一次
     * onStart 当应用程序出现在前台的时候被调用
     * onResume 当应用程序出现在前台的时候被调用
     * onPause 当应用程序退到后台时调用
     * onStop 当应用程序退到后台时调用
     *
     * onDestroy 不调用
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        LogUtil.i("process lifecycle $event")

        //判断 APP 启动  回到前台  退到后台
        when (event) {
            Lifecycle.Event.ON_CREATE -> lastEvent = Lifecycle.Event.ON_CREATE
            Lifecycle.Event.ON_RESUME -> {
                if (lastEvent == Lifecycle.Event.ON_CREATE) {
                    //启动
                    appStart?.invoke()
                } else {
                    //回到前台
                    appForeground?.invoke()
                }
            }
            Lifecycle.Event.ON_STOP -> {
                //回到后台
                appBackground?.invoke()
                lastEvent = Lifecycle.Event.ON_STOP
            }
            else -> {
            }
        }
    }
}