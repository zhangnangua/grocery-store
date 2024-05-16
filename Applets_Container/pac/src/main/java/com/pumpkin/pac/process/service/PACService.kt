package com.pumpkin.pac.process.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.pumpkin.data.AppUtil
import com.pumpkin.data.thread.ThreadHelper
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.WebViewPool
import com.pumpkin.pac.process.ProcessUtil

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
                TYPE_PARSE_DATA -> parseData(callback)
                TYPE_PRELOAD -> preload()
            }

        }

        /**
         * 数据爬取
         */
        private fun parseData(callback: ICallback?) {
            // todo 爬虫测试  需要切换到主线程
//            ThreadHelper.runOnUiThread {
//                ParseEngine(AppUtil.application).loadUrl("https://yandex.com/games/")
//            }
//            callback?.callback(RESULT_SUCCESS, TYPE_PARSE_DATA, "")
        }

        /**
         * 预热
         */
        private fun preload() {
            ThreadHelper.runOnUiThread {
                WebViewPool.preLoad()
            }
        }

    }

    companion object {
        //爬取数据开始
        const val TYPE_PARSE_DATA = "parse.data"

        //预热
        const val TYPE_PRELOAD = "preload"

        //result
        const val RESULT_SUCCESS = 1
    }
}