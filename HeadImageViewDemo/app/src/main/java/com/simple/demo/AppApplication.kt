package com.simple.demo

import android.app.Application
import com.itheima.roundedimageview.BuildConfig
import com.simple.demo.head.HeadImageViewHelp
import com.simple.demo.util.AppUtil

/**
 * 作者： zxf
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtil.inject(this, BuildConfig.DEBUG)

        initHeadSetting()
    }

    private fun initHeadSetting(){
        HeadImageViewHelp.animatorTime = 500
    }
}