package com.pumpkin.pac.process.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.process.ProcessUtil
import com.pumpkin.ui.util.AppUtil

/**
 * pac 进程的预热service
 */
class PACService : Service() {
    override fun onBind(intent: Intent?): IBinder = PACServiceImpl()

    class PACServiceImpl : IPACService.Stub() {
        companion object {
            private const val TAG = "PACSImpl"
        }

        override fun handle(action: String?, callback: ICallback?) {
            if (AppUtil.isDebug) {
                ("current pid ${ProcessUtil.pId()} " +
                        ", current process name is ${ProcessUtil.obtainPACProcessName()} " +
                        "is game process ${ProcessUtil.isPACProcess()}" +
                        ",action is $action").toLogD(TAG)
            }
            // TODO: 待处理
        }

    }
}