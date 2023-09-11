package com.pumpkin.pac

import com.pumpkin.pac.pool.WebViewPool
import com.pumpkin.pac.process.ProcessUtil

/**
 * pac启动初始化
 */
object PACPreload {
    fun pacPreload() {
        /**
         * 在游戏进程 初始化
         */
        if (ProcessUtil.isPACProcess()) {
            WebViewPool.preLoad()
        }
    }
}