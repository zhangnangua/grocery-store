package com.zxf.jetpackrelated.lifecycle.mock

import com.zxf.utils.LogUtil

/**
 * 作者： zxf
 * 描述： 模拟位置提供
 */
object MockLocationManager {

    /**
     * 初始化位置管理器
     */
    fun initLocationManager(){
        LogUtil.i("init location")
    }

    /**
     * 开始获取位置信息
     */
    fun startGetLocation(){
        LogUtil.i("start get location")
    }

    /**
     * 停止获取用户信息
     */
    fun stopGetLocation(){
        LogUtil.i("stop get location")
    }
}