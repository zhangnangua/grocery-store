package com.zxf.jetpackrelated

import android.app.Application
import android.view.Choreographer
import androidx.lifecycle.ProcessLifecycleOwner
import com.zxf.jetpackrelated.lifecycle.application.ProcessLifecycleObserver
import com.zxf.jetpackrelated.utils.AppUtil
import com.zxf.jetpackrelated.utils.LogUtil

/**
 * 作者： zxf
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
                LogUtil.i("app start")
            }, {
                //app进入前台
                LogUtil.i("app foreground")
            }, {
                //app进入后台
                LogUtil.i("app background")
            })
        })


        //Choreographer  test
        //postFrameCallback   Posts a frame callback to run on the next frame. The callback runs once then is automatically removed.
//        Choreographer.getInstance().postFrameCallback {
//            LogUtil.i("frameframeframeframeframeframeframeframeframeframeframe:$it")
//        }
    }
}