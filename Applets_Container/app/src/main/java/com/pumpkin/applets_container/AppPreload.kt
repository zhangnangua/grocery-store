package com.pumpkin.applets_container

import com.pumpkin.applets_container.helper.VHRegister
import com.pumpkin.data.fb.FBFetch
import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.process.ProcessUtil

object AppPreload {
    fun preload(){
        if (ProcessUtil.isMainProcess()) {
            //fb data pull
            FBFetch().fetchListener()
            //app vh register
            VHRegister.register()
            //内置信息 copy
            InternalManager.copy()
        }
    }
}