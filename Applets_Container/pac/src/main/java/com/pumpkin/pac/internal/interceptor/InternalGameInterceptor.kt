package com.pumpkin.pac.internal.interceptor

import android.util.Log
import android.webkit.WebResourceResponse
import com.pumpkin.pac.BuildConfig
import com.pumpkin.webCache.base.DefaultInterceptor
import com.pumpkin.webCache.interceptor.Chain
import com.pumpkin.webCache.requestHelper.CacheHelper
import com.pumpkin.webCache.requestHelper.RequestHelper
import java.io.File

/**
 * internal game
 */
internal class InternalGameInterceptor : DefaultInterceptor() {

    override fun findResource(chain: Chain): WebResourceResponse? {
        val url = chain.url
        val path = CacheHelper.urlToPath(chain.application, url, chain.originUrl) ?: return null
        val file = File(path)
        if (!file.isDirectory) {
            val response = RequestHelper.resourceResponseByFile(url, file)
            if (response != null) {
                return response
            }
        }

        if (BuildConfig.DEBUG) {
            Log.d(TAG, "findResource not  () -> $url")
        }

        return WebResourceResponse(null, null, null)
    }

    companion object {
        const val TAG = "IGI"
    }

}