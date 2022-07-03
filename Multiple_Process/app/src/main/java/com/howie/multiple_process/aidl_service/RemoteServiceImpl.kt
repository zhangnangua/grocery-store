package com.howie.multiple_process.aidl_service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import com.howie.multiple_process.IRemoteService

class RemoteService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder = RemoteServiceImpl

}

object RemoteServiceImpl : IRemoteService.Stub() {

    /**
     * 获取服务端进程ID
     */
    override fun getPid(): Int = Process.myPid()

    override fun basicTypes(
        anInt: Int,
        aLong: Long,
        aBoolean: Boolean,
        aFloat: Float,
        aDouble: Double,
        aString: String?
    ) {
        // TODO: DO
    }
}