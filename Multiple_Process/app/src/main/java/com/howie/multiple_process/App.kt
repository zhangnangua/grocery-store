package com.howie.multiple_process

import android.app.Application
import com.howie.multiple_process.helper.AppUtil

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppUtil.inject(this, BuildConfig.DEBUG)
    }

}