package com.pumpkin.pac.pool

import android.content.Context
import android.content.MutableContextWrapper
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.view.ViewParent
import com.pumpkin.pac.process.ProcessUtil
import com.pumpkin.pac_core.webview.PACWebView
import com.pumpkin.ui.util.AppUtil

/**
 * @author pumpkin
 *
 * todo 支持预加载改造
 */
object WebViewPool {
    private const val TAG = "WebViewPool"
    private const val MAX_NUM = 1

    private val stack = ArrayDeque<PACWebView>(MAX_NUM)

    fun preLoad() {
        Looper.myQueue().addIdleHandler {
            synchronized(stack) {
                if (stack.size < MAX_NUM) {
                    stack.addFirst(createWebView())
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
    fun obtain(context: Context, id: String): PACWebView {
        if (AppUtil.isDebug) {
            Log.d(TAG, "obtain: the current stack count is ${stack.size}")
        }
        var webView: PACWebView
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
        val webViewContext: Context = webView.context
        if (webViewContext is MutableContextWrapper) {
            webViewContext.baseContext = context
        }
        return webView
    }


    fun recycle(webView: PACWebView?) {
        if (webView == null) {
            return
        }
        val context: Context = webView.context
        if (context is MutableContextWrapper) {
            context.baseContext = AppUtil.application
            val parent: ViewParent = webView.parent
            if (parent is ViewGroup) {
                parent.removeView(webView)
            }
            try {
                webView.stopLoading()
                webView.clearHistory()
                webView.clearFormData()
                webView.loadUrl("about:blank")
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
    }

    private fun createWebView(): PACWebView {
        val contextWrapper = MutableContextWrapper(AppUtil.application)
        val cacheWebView: PACWebView
        try {
            cacheWebView = newWV(contextWrapper)
        } catch (e: Exception) {
            throw e
        }
        return cacheWebView
    }


    private fun newWV(contextWrapper: MutableContextWrapper): PACWebView =
        PACWebView(contextWrapper)

}