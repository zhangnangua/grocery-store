package com.pumpkin.webCache

import android.text.TextUtils
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import com.pumpkin.webCache.interceptor.DefaultChain
import com.pumpkin.webCache.interceptor.Interceptor
import com.pumpkin.webCache.interceptors.EndInterceptor

class Engine(private val client: WVCacheClient) {

    private val interceptors = ArrayList<Interceptor>().apply {
        val list = client.getInterceptors()
        if (list != null) {
            addAll(list)
        }
        add(EndInterceptor())
    }

    fun interceptRequest(request: WebResourceRequest?): WebResourceResponse? {
        if (request == null) {
            return null
        }
        val uri = request.url ?: return null


        //非Get请求，不进行拦截
        return if (!TextUtils.equals(request.method, "GET")) {
            null
        } else DefaultChain(interceptors,
            0,
            request,
            uri.toString(),
            client.getOriginUrl(),
            client.getResourceId(),
            client.getApplication())
            .process()
    }

}
