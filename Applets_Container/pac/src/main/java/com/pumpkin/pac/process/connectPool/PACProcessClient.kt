package com.pumpkin.pac.process.connectPool

import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.process.service.PACService
import com.pumpkin.pac_core.BuildConfig

/**
 * pac 进程链接
 */
object PACProcessClient : ConnectPool<IPACService>(true) {

    private const val TAG = "GameConnectPool"

    override fun asInterface(service: IBinder?): IPACService? =
        IPACService.Stub.asInterface(service)

    override fun createConnectServiceIntent(): Intent =
        Intent(AppUtil.application, PACService::class.java)

    private fun handle(action: String, callBack: ICallback.Stub? = null): Boolean {
        return execute {
            it?.handle(action, object : ICallback.Stub() {
                override fun callback(code: Int, action: String?, message: String?) {
                    if (BuildConfig.DEBUG) {
                        callBack?.callback(code, action, message)
                    }
                }

            })
        }
    }

    fun warmUp(){
        connect()
    }

    override fun serviceConnected(service: IBinder?, serviceConnection: ServiceConnection) {
        //调度到子进程  爬虫 拉取数据
        handle(PACService.TYPE_PARSE_DATA)
        //web 预热
        handle(PACService.TYPE_PRELOAD)
    }


}
