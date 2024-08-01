package com.pumpkin.pac

import com.pumpkin.pac.internal.InternalManager
import com.pumpkin.pac.process.ProcessUtil

/**
 * pac启动初始化
 */
object PACPreload {
    fun pacPreload() {
        if (ProcessUtil.isPACProcess()) {
            //内置信息 copy

        }
    }
}