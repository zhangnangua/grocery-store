package com.pumpkin.applets_container

import android.app.Application
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.PACPreload
import com.pumpkin.pac.util.GameHelper

class PACApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppUtil.inject(this, BuildConfig.DEBUG, GameHelper)

        preload()
    }

    private fun preload() {
        //app preload
        AppPreload.preload()

        //game  preload
        PACPreload.pacPreload()
    }

}