package com.pumpkin.applets_container

import com.pumpkin.applets_container.helper.VHRegister
import com.pumpkin.data.fb.FBFetch
import com.pumpkin.pac.process.ProcessUtil

object AppPreload {
    fun preload(){

        //fb data pull
        if (ProcessUtil.isMainProcess()) {
            FBFetch().fetchListener()
        }
        //app vh register
        VHRegister.register()
    }
}