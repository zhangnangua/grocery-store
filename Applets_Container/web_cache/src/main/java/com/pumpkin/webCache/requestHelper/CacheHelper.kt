package com.pumpkin.webCache.requestHelper

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import com.pumpkin.webCache.util.MD5Utils
import java.io.File

/**
 * howie
 * <scheme>://<authority><path>?<query>
 * [scheme:][//host:port][path][?query][#fragment]
 */
object CacheHelper {
    private const val CACHE_DYNAMIC_FILE_NAME = "pac"
    private const val CACHE_ZIP_FILE_NAME = "pac_zip"

    private const val CONFIG_FILE_GAME_SUFFIX = "_pac_zip_config";

    /**
     * 默认1个G
     */
    const val DYNAMIC_CACHE_SIZE = 2 * 1024 * 1024 * 1024L

    fun getDynamicCacheFile(context: Context) =
        File("${context.filesDir}${File.separator}$CACHE_DYNAMIC_FILE_NAME")

    fun urlToPath(context: Context, url: String?, rootUrl: String): String? {
        val rootRemoveQueryAndFragmentUri = removeQueryAndFragment(rootUrl) ?: return null
        val removeQueryAndFragmentUri = removeQueryAndFragment(url) ?: return null
        val urlToPath = urlToPathInternal(removeQueryAndFragmentUri)
        return destinationFolder(context, rootRemoveQueryAndFragmentUri) + File.separator + urlToPath
    }

    fun outPath(context: Context, rootUrl: String?): String? {
        val rootRemoveQueryAndFragmentUri = removeQueryAndFragment(rootUrl) ?: return null
        return destinationFolder(context, rootRemoveQueryAndFragmentUri)
    }

    fun unzipSuccess(outPath: String, id: String): Boolean {
        val config = File(outPath + File.separator + id + CONFIG_FILE_GAME_SUFFIX)
        return config.createNewFile()
    }

    fun isUnzipSuccess(outPath: String, id: String) = File(outPath + File.separator + id + CONFIG_FILE_GAME_SUFFIX).exists()

    private fun destinationFolder(context: Context, removeQueryAndFragmentUri: Uri): String? {
        val zipId = zipId(removeQueryAndFragmentUri) ?: return null
        return getRootPath(context) + File.separator + zipId
    }


    private fun getRootPath(context: Context) =
        context.filesDir.toString() + File.separator + CACHE_ZIP_FILE_NAME


    private fun urlToUri(url: String?): Uri? {
        if (url == null) {
            return null
        }
        return try {
            Uri.parse(url)
        } catch (e: Exception) {
            null
        }
    }

    private fun zipId(removeQueryAndFragmentUri: Uri?): String? {
        val nakedUlr = removeQueryAndFragmentUri?.toString() ?: return null
        return MD5Utils.md5(nakedUlr)
    }

    private fun urlToPathInternal(removeQueryAndFragmentUri: Uri): String {
        var result: String = removeQueryAndFragmentUri.toString()

        val scheme = removeQueryAndFragmentUri.scheme
        if (!TextUtils.isEmpty(scheme)) {
            val schemePrefix = "$scheme://"
            result = result.replace(schemePrefix, "")
        }
        //if the last character is /,remove
        if (result.endsWith("/")) {
            result = result.substring(0, result.length - 1)
        }
        return result
    }

    private fun removeQueryAndFragment(url: String?): Uri? {
        val uri = urlToUri(url) ?: return null
        return removeQueryAndFragment(uri)
    }

    private fun removeQueryAndFragment(uri: Uri): Uri {
        return uri.buildUpon().clearQuery().fragment(null).build()
    }

}