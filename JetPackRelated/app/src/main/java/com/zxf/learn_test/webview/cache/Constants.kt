package com.zxf.learn_test.webview.cache

/**
 * 文件默认文件夹
 */
const val CACHE_DEFAULT_FILE_NAME: String = "WebViewCacheResource"

/**
 * 资源拦截key
 */
const val CACHE_REQUEST_HEAD : String = "WebResourceInterceptorKey"


enum class CacheType {
    NOCACHE,
    CACHE
}