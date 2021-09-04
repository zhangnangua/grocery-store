package com.zxf.jetpackrelated.lifecycle.activityOrfragment

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * 作者： zxf
 * 描述： lifecycleFragment demo
 */
class LifecycleFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationLifecycleListener = LocationLifecycleListener {
            // TODO: 2021/9/2 模拟接收地理数据
        }
        lifecycle.addObserver(locationLifecycleListener)
    }

}