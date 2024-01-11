package com.pumpkin.webCache.requestHelper

interface InterceptorConfig {

    fun isCache(url: String): Boolean

    fun isAdvertise(url: String): Boolean

    fun cacheDay(): String

    fun dayToMaxAge(day: Long): String {
        return "max-age=" + 86400L * day
    }
}

object DefaultInterceptorConfig : InterceptorConfig {
    override fun isCache(url: String) = true

    override fun isAdvertise(url: String) = url.contains("googleads")
            || url.contains("googlesyndication")
            || url.contains("google-analytics")
            || url.contains("athena")
            || url.contains("hisavana")
            || url.contains("pay-japi.ahagamecenter")

    /**
     * 默认有效时间100年
     */
    override fun cacheDay(): String {
        return dayToMaxAge(365 * 100)
    }
}
