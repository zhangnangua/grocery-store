package com.pumpkin.pac.process.connectPool

import android.content.Intent
import android.os.IBinder
import com.pumpkin.pac.ICallback
import com.pumpkin.pac.IPACService
import com.pumpkin.pac.process.service.PACService
import com.pumpkin.ui.util.AppUtil

/**
 * pac 进程链接
 */
object GameConnectPool : ConnectPool<IPACService>(true) {
    override fun asInterface(service: IBinder?): IPACService? =
        IPACService.Stub.asInterface(service)

    override fun createConnectServiceIntent(): Intent =
        Intent(AppUtil.application, PACService::class.java)

    fun handle(action: String): Boolean {
        return execute {
            it?.handle(action, object : ICallback.Stub() {
                override fun callback(code: Int, action: String?, message: String?) {
                    // TODO: 结果回调
                }

            })
        }
    }

}
