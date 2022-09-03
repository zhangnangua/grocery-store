package com.zxf.learn_test.webview.cache

import android.text.TextUtils

/**
 * 如果要个性化缓存文件类型，需要实现 {@link ICacheExtension}
 */
interface ICacheExtension {
    /**
     * 判断是媒体文件，媒体文件均不走缓存
     */
    fun isMedia(extension: String): Boolean

    /**
     * 判断是能缓存的文件
     */
    fun canCache(extension: String): Boolean

    /**
     * 判断是否是html
     */
    fun isHtml(extension: String): Boolean
}

/**
 * 默认缓存实现
 */
object CacheExtensionConfig : ICacheExtension {

    /**
     * 可以被缓存的资源
     */
    private val CACHE = arrayOf(
        "js",
        "ico",
        "css",
        "png",
        "jpg",
        "jpeg",
        "gif",
        "bmp",
        "ttf",
        "woff",
        "woff2",
        "otf",
        "eot",
        "svg",
        "xml",
        "swf",
        "txt",
        "text",
        "conf",
        "webp"
    )

    /**
     * 不进行缓存的资源
     */
    private val NO_CACHE = arrayOf(
        "mp4",
        "mp3",
        "ogg",
        "jpg",
        "jpeg",
        "gif",
        "png",
        "avi",
        "wmv",
        "flv",
        "rmvb",
        "3gp"
    )

    override fun isMedia(extension: String): Boolean {
        var extensionSnap = extension
        if (TextUtils.isEmpty(extensionSnap)) {
            return false
        }
        extensionSnap = extensionSnap.toLowerCase().trim()
        return NO_CACHE.contains(extensionSnap)
    }

    override fun canCache(extension: String): Boolean {
        var extensionSnap = extension
        if (TextUtils.isEmpty(extensionSnap)) {
            return false
        }
        extensionSnap = extensionSnap.toLowerCase().trim()
        return CACHE.contains(extensionSnap)
    }

    override fun isHtml(extension: String): Boolean {
        if (TextUtils.isEmpty(extension)) {
            return false
        }
        val extensionSnap = extension.toLowerCase().trim()
        return extensionSnap.contains("html") || extensionSnap.contains("htm")
    }


}