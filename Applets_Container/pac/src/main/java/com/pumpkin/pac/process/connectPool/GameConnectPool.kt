package com.pumpkin.pac.process.connectPool

import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.process.service.PACService

/**
 * pac 进程链接
 */
object GameConnectPool : ConnectPool<IPACService>(true) {

    private const val TAG = "GameConnectPool"

    override fun asInterface(service: IBinder?): IPACService? =
        IPACService.Stub.asInterface(service)

    override fun createConnectServiceIntent(): Intent =
        Intent(AppUtil.application, PACService::class.java)

    fun handle(action: String): Boolean {
        return execute {
            it?.handle(action, object : ICallback.Stub() {
                override fun callback(code: Int, action: String?, message: String?) {
                    Log.d(TAG, "callback () -> $code , $action , $message")
                }

            })
        }
    }

    override fun serviceConnected(service: IBinder?, serviceConnection: ServiceConnection) {
        //最近游玩数据查询


        //爬虫 拉取数据
        handle(PACService.PARSE_DATA)
    }

}
