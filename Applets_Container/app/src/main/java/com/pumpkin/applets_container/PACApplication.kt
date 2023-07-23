package com.pumpkin.applets_container

import android.app.Application
import com.pumpkin.ui.util.AppUtil

class PACApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppUtil.inject(this, BuildConfig.DEBUG)

        preload()
    }

    private fun preload() {
        Preload.pacPreload()
    }

}