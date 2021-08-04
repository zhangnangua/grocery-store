package com.simple.dragdeletesample

import android.app.Application
import androidx.viewbinding.BuildConfig
import com.simple.dragdeletesample.util.AppUtil

/**
 * 作者： zxf
 */
class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtil.inject(this, BuildConfig.DEBUG)
    }
}