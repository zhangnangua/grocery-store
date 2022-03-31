package com.pumpkin.automatic_execution

import android.app.Application
import android.view.Choreographer
import androidx.lifecycle.ProcessLifecycleOwner
import com.pumpkin.automatic_execution.observer.ProcessLifecycleObserver
import com.pumpkin.automatic_execution.util.AppUtil
import com.pumpkin.automatic_execution.util.LogUtil

/**
 * 作者： pumpkin
 * 描述： application
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //appUtil  注册
        AppUtil.inject(this, BuildConfig.DEBUG)
        //process 生命周期注册
        ProcessLifecycleOwner.get().lifecycle.addObserver(ProcessLifecycleObserver().also { processLifecycleObserver ->
            processLifecycleObserver.inject({
                //app启动
                LogUtil.i("app cold start")
            }, {
                //app进入前台
                LogUtil.i("app foreground")
            }, {
                //app进入后台
                LogUtil.i("app background")
            })
        })


    }
}