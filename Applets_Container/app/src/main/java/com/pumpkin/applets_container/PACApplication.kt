package com.pumpkin.applets_container

import android.app.Application
import com.pumpkin.mvvm.util.AppUtil
import com.pumpkin.pac.pool.WebViewPool

class PACApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppUtil.inject(this, BuildConfig.DEBUG)
        WebViewPool.preLoad()

    }

}