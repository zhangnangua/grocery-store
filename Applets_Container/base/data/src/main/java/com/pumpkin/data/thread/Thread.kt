package com.pumpkin.data.thread

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object ThreadHelper {
    private val MAIN_HANDLER = Handler(Looper.getMainLooper())

    /**
     * 当前线程是否为主线程
     */
    fun isUiThread(): Boolean {
        return Thread.currentThread() === Looper.getMainLooper().thread
    }

    fun getMainThreadHandler(): Handler? {
        return MAIN_HANDLER
    }


    /**
     * 在主线程中运行
     */
    fun runOnUiThread(runnable: Runnable) {
        MAIN_HANDLER.post(runnable)
    }

    /**
     * 如果运行在主线程，则立即执行
     */
    fun runOnUiThreadImmediate(runnable: Runnable) {
        if (isUiThread()) {
            runnable.run()
        } else {
            runOnUiThread(runnable)
        }
    }

}

internal class ContextScope(context: CoroutineContext) : CoroutineScope {
    override val coroutineContext: CoroutineContext = context
    override fun toString(): String = "CoroutineScope(coroutineContext=$coroutineContext)"
}

fun IoScope(): CoroutineScope = ContextScope(SupervisorJob() + Dispatchers.IO)