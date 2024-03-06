package com.pumpkin.pac

import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.pool.WebViewPool
import com.pumpkin.pac.process.ProcessUtil

/**
 * pac启动初始化
 */
object PACPreload {
    fun pacPreload() {
        //在游戏进程 初始化
        if (ProcessUtil.isPACProcess()) {
            // TODO: do
//            WebViewPool.preLoad()
        }

        if (ProcessUtil.isMainProcess()) {
            //内置信息 copy
            InternalManager.copy()
        }
    }
}