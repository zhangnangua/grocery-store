package com.howie.multiple_process.view.general_lifecycle

import android.app.Activity
import android.os.SystemClock
import android.util.Log
import com.howie.multiple_process.helper.AppUtil
import com.howie.multiple_process.helper.BrowserHelper

/**
 * 用于感知打开三方应用的生命周期，执行时常等等
 *
 * 该界面窗口设置为透明
 */
class GeneralLifecycleActivity : Activity() {

    private var state: State = State.Initialize

    private var startTime: Long? = null

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: currentState is ${state.javaClass.simpleName}")
        when (state) {
            State.Activated -> tryFinish()
            State.Destroy -> throw IllegalStateException("it is already destroyed.")
            State.Initialize -> tryActivated()
        }
    }

    private fun tryActivated() {
        // TODO: 打开三方应用
        if (BrowserHelper.openUrlByBrowser("https://www.baidu.com", AppUtil.application)) {
            state = State.Activated
            startTime = SystemClock.elapsedRealtime()
            return
        }
        finish()
    }

    private fun tryFinish() {
        // TODO: 回来
        state = State.Destroy
        val consumeTime = SystemClock.elapsedRealtime() - startTime!!
        Log.d(TAG, "consumeTime: $consumeTime , currentState is ${state.javaClass.simpleName}")
        finish()
    }


    sealed class State {
        object Initialize : State()
        object Activated : State()
        object Destroy : State()
    }

    companion object {
        const val TAG = "GeneralLifecycle"
    }
}