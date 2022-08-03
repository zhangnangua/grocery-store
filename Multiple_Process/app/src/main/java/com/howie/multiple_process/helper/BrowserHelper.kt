package com.howie.multiple_process.helper

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.howie.multiple_process.bean.Book

object BrowserHelper {

    /**
     * 通过浏览器打开三方应用
     */
    fun openUrlByBrowser(url: String, context: Context): Boolean {
        val uri = Uri.parse(url)
        if (uri != null && verifyUri(uri)) {
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                if (context is Application) {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }

            return try {
                context.startActivity(intent)
                true
            } catch (e: Exception) {
                false
            }
        }
        return false
    }

    fun verifyUri(uri: Uri): Boolean {
        val scheme = uri.scheme
        return uri.isHierarchical && scheme?.run { startsWith("http ") || startsWith("https") } ?: false
    }
}