package com.pumpkin.pac.process.connectPool

import android.app.Application
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.IInterface
import com.pumpkin.mvvm.util.toLogD
import com.pumpkin.ui.util.AppUtil
import java.util.concurrent.locks.ReentrantReadWriteLock

/**
 * 进程通用链接
 */
abstract class ConnectPool<iInterface : IInterface>(private val retry: Boolean) {

    private var iService: iInterface? = null

    private var state: Int = STATE_UNCONNECTED

    private val application: Application

    private val writeLock: ReentrantReadWriteLock.WriteLock
    private val readLock: ReentrantReadWriteLock.ReadLock

    init {
        val lock = ReentrantReadWriteLock()
        writeLock = lock.writeLock()
        readLock = lock.readLock()
        application = AppUtil.application
    }

    companion object {
        private const val TAG = "ConnectPool"

        const val STATE_UNCONNECTED = -1
        const val STATE_CONNECTING = 1
        const val STATE_CONNECTED = -1
    }

    fun connect() {

        readLock.lock()
        if (state == STATE_CONNECTING) {
            return
        }
        readLock.unlock()

        writeLock.lock()
        state = STATE_CONNECTING
        writeLock.unlock()

        connectInternal()
    }

    private fun connectInternal() {
        val intent = createConnectServiceIntent()
        val newConnect = newConnect()
        if (!bindService(intent, newConnect)) {
            connectInvalid(newConnect)
        }
    }


    private fun newConnect() = object : ServiceConnection {
        //客户端 主线程 被回调
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (AppUtil.isDebug) {
                "onServiceConnected()-> ".toLogD(TAG)
            }
            if (service != null) {
                serviceConnectedInternal(service, this)
            } else {
                connectInvalid(this)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            if (AppUtil.isDebug) {
                "onServiceDisconnected()-> ".toLogD(TAG)
            }
            connectInvalid(this)
        }

        override fun onBindingDied(name: ComponentName?) {
            super.onBindingDied(name)
            if (AppUtil.isDebug) {
                "onBindingDied()-> ".toLogD(TAG)
            }
            connectInvalid(this)
        }

        override fun onNullBinding(name: ComponentName?) {
            super.onNullBinding(name)
            if (AppUtil.isDebug) {
                "onNullBinding()-> ".toLogD(TAG)
            }
            connectInvalid(this)
        }
    }

    private fun serviceConnectedInternal(service: IBinder, serviceConnection: ServiceConnection) {
        writeLock.lock()
        iService = asInterface(service)
        state = STATE_CONNECTED
        writeLock.unlock()

        serviceConnected(service, serviceConnection)
    }

    private fun connectInvalid(serviceConnection: ServiceConnection) {
        writeLock.lock()
        state = STATE_UNCONNECTED
        iService = null
        writeLock.unlock()

        unbindService(serviceConnection)

        if (retry) {
            connect()
        }

    }


    private fun isAlive(): Boolean {
        readLock.lock()
        val b = state == STATE_CONNECTED && iService != null && iService!!.asBinder().isBinderAlive
        readLock.unlock()
        return b
    }

    fun execute(logic: (iInterface: iInterface?) -> Unit): Boolean {
        return if (isAlive()) {
            logic.invoke(iService)
            true
        } else {
            if (retry) {
                connect()
            }
            false
        }
    }

    private fun unbindService(serviceConnection: ServiceConnection) {
        try {
            application.unbindService(serviceConnection)
        } catch (e: Exception) {
            if (AppUtil.isDebug) {
                "unbindService()->".toLogD(TAG)
            }
        }
    }

    private fun bindService(intent: Intent, connection: ServiceConnection): Boolean {
        return try {
            //通过bind的方式进行调用，当没有任何client与Service绑定时，Service会自行销毁 。
            //当client销毁时，client会自动与Service解除绑定。
            //当然，client也可以明确调用Context的unbindService()方法与Service解除绑定
            application.bindService(intent, connection, Context.BIND_AUTO_CREATE)
        } catch (e: Exception) {
            false
        }
    }

    protected open fun serviceConnected(service: IBinder?, serviceConnection: ServiceConnection) {

    }

    protected abstract fun asInterface(service: IBinder?): iInterface?

    protected abstract fun createConnectServiceIntent(): Intent
}