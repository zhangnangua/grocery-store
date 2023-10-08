package com.pumpkin.applets_container

import android.app.Application
import com.pumpkin.data.AppUtil
import com.pumpkin.data.fb.FBFetch
import com.pumpkin.pac.PACPreload
import com.pumpkin.pac.process.ProcessUtil

class PACApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AppUtil.inject(this, BuildConfig.DEBUG)

        preload()
    }

    private fun preload() {
        //fb data pull
        if (ProcessUtil.isMainProcess()) {
            FBFetch().fetchListener()
        }

        //game process preload
        PACPreload.pacPreload()
    }

}