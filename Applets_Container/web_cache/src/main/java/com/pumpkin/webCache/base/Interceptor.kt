package com.pumpkin.webCache.interceptor

import android.app.Application
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse

interface Interceptor {
    fun intercept(chain: Chain): WebResourceResponse?
}

interface Chain {

    val request: WebResourceRequest

    val url: String

    fun process(): WebResourceResponse?

    val application: Application

    val originUrl: String?

    val resourceId: String?

}

class DefaultChain(private val interceptors: List<Interceptor>,
                   private val index: Int,
                   override val request: WebResourceRequest,
                   override val url: String,
                   override val originUrl: String?,
                   override val resourceId: String?,
                   override val application: Application) : Chain {

    override fun process(): WebResourceResponse? {
        require(index < interceptors.size) { "index cannot be greater than interceptors length." }
        val nextChain = copy(index + 1)
        val interceptor: Interceptor = interceptors[index]
        return interceptor.intercept(nextChain)
    }

    private fun copy(index: Int): Chain {
        return DefaultChain(interceptors, index, request, url, originUrl, resourceId, application)
    }
}
