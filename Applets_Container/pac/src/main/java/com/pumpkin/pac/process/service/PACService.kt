package com.pumpkin.pac.process.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pumpkin.data.AppUtil
import com.pumpkin.data.thread.ThreadHelper
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.process.ProcessUtil
import com.pumpkin.parse.ParseEngine

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
            when (action) {
                PARSE_DATA -> parseData(callback)
            }

        }

        /**
         * 数据爬取
         */
        private fun parseData(callback: ICallback?) {
            // todo 爬虫测试  需要切换到主线程
            ThreadHelper.runOnUiThread{
                ParseEngine(AppUtil.application).loadUrl("https://yandex.com/games/")
            }
            callback?.callback(RESULT_SUCCESS, PARSE_DATA, "")
        }

    }

    companion object {
        //爬取数据开始
        const val PARSE_DATA = "parse.data"

        //result
        const val RESULT_SUCCESS = 1
    }
}