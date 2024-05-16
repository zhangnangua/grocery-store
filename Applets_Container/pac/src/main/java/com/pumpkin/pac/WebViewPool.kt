package com.pumpkin.pac

import android.content.Context
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.ViewParent
import com.pumpkin.data.AppUtil
import com.pumpkin.pac.process.ProcessUtil
import com.pumpkin.pac_core.webview.PACWebEngine

/**
 * @author pumpkin
 *
 * todo 支持预加载改造
 */
object WebViewPool {
    private const val TAG = "WebViewPool"
    private const val MAX_NUM = 1

    private val stack = ArrayDeque<PACWebEngine>(MAX_NUM)

    fun preLoad() {
        Looper.myQueue().addIdleHandler {
            synchronized(stack) {
                if (stack.size < MAX_NUM) {
                    val createWebView = createWebView() ?: return@synchronized
                    stack.addFirst(createWebView)
                    if (AppUtil.isDebug) {
                        Log.d(
                            TAG,
                            "preLoad -> () , the current stack count is  ${stack.size} , current process name is ${ProcessUtil.obtainPACProcessName()}"
                        )
                    }
                }
            }
            false
        }
    }

    /**
     * 获取一个CacheWebView
     */
    fun obtain(context: Context? = null, isNew: Boolean = false): PACWebEngine? {
        if (isNew) {
            return createWebView(context)
        }
        if (AppUtil.isDebug) {
            Log.d(TAG, "obtain: the current stack count is ${stack.size}")
        }
        var webView: PACWebEngine?
        synchronized(stack) {
            if (!stack.isEmpty()) {
                webView = stack.removeFirst()
                if (AppUtil.isDebug) {
                    Log.d(TAG, "obtain: from stack , the current stack count is ${stack.size}")
                }
            } else {
                webView = createWebView()
                if (AppUtil.isDebug) {
                    if (AppUtil.isDebug) {
                        Log.d(
                            TAG,
                            "obtain: from createWebView , the current stack count is ${stack.size}"
                        )
                    }
                }
            }
        }
        return webView
    }


    fun recycle(webView: PACWebEngine?) {
        if (webView == null) {
            return
        }
        val parent: ViewParent = webView.parent
        if (parent is ViewGroup) {
            parent.removeView(webView)
        }
        try {
            webView.stopLoading()
            webView.clearFormData()
            webView.loadUrl("about:blank")
            webView.clearHistory()
            webView.clear()
            synchronized(stack) {
                //放回缓存池
                if (stack.size < MAX_NUM) {
                    stack.addFirst(webView)
                }
            }
        } catch (ignored: Exception) {
        }
        if (AppUtil.isDebug) {
            Log.d(TAG, "obtain: recycle is success, the current stack count is ${stack.size}")
        }
    }

    private fun createWebView(context: Context? = null): PACWebEngine? {
        return try {
            newWV(context ?: AppUtil.application)
        } catch (e: Exception) {
            null
        }
    }


    private fun newWV(context: Context): PACWebEngine =
        PACWebEngine(context)

}