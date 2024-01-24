package com.pumpkin.webCache.requestHelper

interface InterceptorConfig {

    fun isCache(url: String): Boolean

    fun isOtherInterceptor(url: String): Boolean

    fun cacheDay(): String

    fun dayToMaxAge(day: Long): String {
        return "max-age=" + 86400L * day
    }
}

abstract class BaseInterceptorConfig : InterceptorConfig {
    override fun isCache(url: String) = true
    override fun isOtherInterceptor(url: String) = url.contains("googleads")
            || url.contains("googlesyndication")
            || url.contains("google-analytics")
            || url.contains("athena")
            || url.contains("hisavana")
            || url.contains("pay-japi.ahagamecenter")
}

object DefaultInterceptorConfig : BaseInterceptorConfig() {
    /**
     * 默认有效时间100年
     */
    override fun cacheDay(): String {
        return dayToMaxAge(365 * 100)
    }
}
