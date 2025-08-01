package com.zxf.learn_test.webview.cache

import android.content.Context
import android.os.Build
import android.text.TextUtils
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import com.zxf.utils.AppUtil
import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager


class WebViewCacheInterceptorGlobal(

    /**
     * 缓存所存在的文件
     */
    var cacheFile: File,

    /**
     * 缓存大小
     */
    var cacheSize: Long,

    /**
     * 响应策略， 1:使用服务端默认的响应头，0:客户端拦截添加响应头
     */
    var strategy: Int,

    /**
     * 是否开启缓存能力
     */
    var cacheType: CacheType,

    /**
     * 链接最长延时
     */
    var connectTimeout: Long,

    /**
     * 读最长延时
     */
    var readTimeout: Long,

    /**
     * context
     */
    var context: Context?,

    /**
     * 缓存默认配置
     */
    var cacheExtensionConfig: ICacheExtension,
    /**
     * https 证书相关配置
     */
    var hostnameVerifier: HostnameVerifier?,
    var sslSocketFactory: SSLSocketFactory?,
    var x509TrustManager: X509TrustManager?,

    /**
     * dns
     */
    var dns: Dns?,

    /**
     * 判断所需拦截的资源，默认拦截所有资源
     * true 拦截
     * 默认值为true
     */
    var customInterceptorResource: ((url: String) -> Boolean)?

) : IWebViewRequestInterceptor {

    private constructor(build: Builder) : this(
        build.cacheFile,
        build.cacheSize,
        build.strategy,
        build.cacheType,
        build.connectTimeout,
        build.readTimeout,
        build.context,
        build.cacheExtensionConfig,
        build.hostnameVerifier,
        build.sslSocketFactory,
        build.x509TrustManager,
        build.dns,
        build.customInterceptorResource
    )

    /**
     *  okHttpClient
     */
    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .cache(Cache(cacheFile, cacheSize))
        .callTimeout(connectTimeout, TimeUnit.SECONDS)
        .readTimeout(readTimeout, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpCacheInterceptor())
        .hostnameVerifier { _, _ ->
            true
        }
        .let { build ->
            if (sslSocketFactory != null && x509TrustManager != null) {
                build.sslSocketFactory(sslSocketFactory!!, x509TrustManager!!)
            }
            if (dns != null) {
                build.dns(dns!!)
            }
            build.build()
        }

    /**
     * sdk<21时 需要通过它loadUrl手动设置 相关请求头 Origin Referer UserAgent
     * Origin 用于指明当前请求来自于哪个站点 格式：https://baidu.com
     * Referer 告诉服务器我是从哪个页面链接过来的 格式：一个完成的url
     * UserAgent
     */
    var mOrigin: String = ""
    var mReferer: String = ""
    var mUserAgent: String = ""


    class Builder {
        lateinit var cacheFile: File
            private set
        var cacheSize: Long = 100 * 1024 * 1024L
            private set
        var strategy: Int = 0
            private set
        var cacheType: CacheType = CacheType.CACHE
            private set
        var connectTimeout: Long = 20
            private set
        var readTimeout: Long = 20
            private set
        var hostnameVerifier: HostnameVerifier? = null
            private set
        var sslSocketFactory: SSLSocketFactory? = null
            private set
        var x509TrustManager: X509TrustManager? = null
            private set
        var dns: Dns? = null
            private set
        var cacheExtensionConfig: ICacheExtension = CacheExtensionConfig
            private set
        var customInterceptorResource: ((url: String) -> Boolean)? = null
            private set
        lateinit var context: Context
            private set

        fun setCacheSize(cacheSize: Long) =
            apply { this.cacheSize = cacheSize }

        fun setCacheType(cacheType: CacheType) =
            apply { this.cacheType = cacheType }

        fun setStrategy(strategy: Int) =
            apply { this.strategy = strategy }

        fun setConnectTimeout(connectTimeout: Long) =
            apply { this.connectTimeout = connectTimeout }

        fun setReadTimeout(readTimeout: Long) =
            apply { this.readTimeout = readTimeout }

        fun setHostnameVerifier(hostnameVerifier: HostnameVerifier?) =
            apply { this.hostnameVerifier = hostnameVerifier }

        fun setSocketFactory(sslSocketFactory: SSLSocketFactory?) =
            apply { this.sslSocketFactory = sslSocketFactory }

        fun setX509TrustManager(x509TrustManager: X509TrustManager?) =
            apply { this.x509TrustManager = x509TrustManager }

        fun setDns(dns: Dns?) =
            apply { this.dns = dns }

        fun setCacheExtensionConfig(cacheExtensionConfig: ICacheExtension) =
            apply { this.cacheExtensionConfig = cacheExtensionConfig }

        fun setCustomInterceptorResource(customInterceptorResource: ((url: String) -> Boolean)?) =
            apply { this.customInterceptorResource = customInterceptorResource }

        fun build(): WebViewCacheInterceptorGlobal {
            this.context = AppUtil.application
            this.cacheFile = File(context.cacheDir, CACHE_DEFAULT_FILE_NAME)
            return WebViewCacheInterceptorGlobal(this)
        }
    }

    private fun interceptRequest(
        url: String?,
        headers: MutableMap<String, String>?
    ): WebResourceResponse? {

        var headersSnap = headers

        if (cacheType == CacheType.CACHE && checkUrl(url)) {

            try {
                val requestBuilder = Request.Builder().url(url!!)
                val extension = MimeTypeMapUtils.getFileExtensionFromUrl(url)

                //请求头添加处理
                if (headersSnap == null) {
                    headersSnap = HashMap()

                }
                if (cacheExtensionConfig.isHtml(extension)) {
                    //html 不做缓存处理
                    headersSnap[CACHE_REQUEST_HEAD] = CacheType.NOCACHE.name
                }

                //默认使用客户端的响应策略，强制缓存。只有strategy设置为1的时候，才会使用服务端默认的响应头
                if (strategy == 1) {
                    headersSnap[CACHE_REQUEST_HEAD] = CacheType.NOCACHE.name
                }

                headersSnap.forEach { entry: Map.Entry<String, String> ->
                    requestBuilder.addHeader(entry.key, entry.value)
                }

                if (!NetUtils.isConnected(context)) {
                    requestBuilder.cacheControl(CacheControl.FORCE_CACHE)
                }

                val request = requestBuilder.build()
                val response: Response = okHttpClient.newCall(request).execute()
                if (response.code == 504 && !NetUtils.isConnected(context)) {
                    return null
                }
                //请求处理
                val mimeType = MimeTypeMapUtils.getMimeTypeFromUrl(url)
                val webResourceResponse =
                    WebResourceResponse(mimeType, "", response.body?.byteStream())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    var message = response.message
                    if (TextUtils.isEmpty(message)) {
                        message = "OK"
                    }
                    try {
                        webResourceResponse.setStatusCodeAndReasonPhrase(response.code, message)
                    } catch (e: Exception) {
                        return null
                    }
                    webResourceResponse.responseHeaders =
                        NetUtils.multimapToSingle(response.headers.toMultimap())

//                    webResourceResponse.responseHeaders = mapOf(
//                        "Access-Control-Allow-Origin" to "*",
//                        "Access-Control-Allow-Headers" to "*",
//                        "Access-Control-Allow-Credentials" to "true",
//                        "Access-Control-Allow-Methods" to "POST, PUT, GET, OPTIONS, DELETE"
//                        )
                }
                return webResourceResponse
            } catch (e: Exception) {
                return null
            }

        }
        return null
    }


    override fun interceptRequest(request: WebResourceRequest?): WebResourceResponse? {
        if (request != null) {
            return interceptRequest(request.url.toString(), request.requestHeaders)
        }
        return null
    }

    override fun interceptRequest(url: String?) = interceptRequest(
        url, mapOf(
            "Origin" to mOrigin,
            "Referer" to mReferer,
            "User-Agent" to mUserAgent
        ) as MutableMap<String, String>
    )

    private fun checkUrl(url: String?): Boolean {
        if (TextUtils.isEmpty(url) || !url!!.startsWith("http")) {
            return false
        }
        if (customInterceptorResource != null) {
            return customInterceptorResource!!.invoke(url)
        }

        val extension = MimeTypeMapUtils.getFileExtensionFromUrl(url)

        if (TextUtils.isEmpty(extension)) {
            return false
        }

        if (cacheExtensionConfig.isMedia(extension)) {
            return false
        }
        if (!cacheExtensionConfig.canCache(extension)) {
            return false
        }

        return true
    }

    private fun isValidUrl(url: String?): Boolean {
        return URLUtil.isValidUrl(url)
    }

    override fun loadUrl(webView: WebView, url: String) {
        if (!isValidUrl(url)) {
            return
        }
        webView.loadUrl(url)
        mReferer = webView.url!!
        mOrigin = NetUtils.getOriginUrl(mReferer)
        mUserAgent = webView.settings.userAgentString
    }

    override fun enableCache(enable: Boolean) {
        cacheType = if (enable) {
            CacheType.CACHE
        } else {
            CacheType.NOCACHE
        }
    }
}
