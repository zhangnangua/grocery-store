package com.pumpkin.content_provider_start1

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import android.util.ArrayMap
import android.util.Log

class ContentProviderStart1 : ContentProvider() {
    override fun onCreate(): Boolean {
        // TODO: 2022/4/10 不建议此处做耗时操作，比如读写数据库等等 。
        Log.i("ContentProviderStart1", "onCreate: ${System.currentTimeMillis()}")

        // TODO: 2022/4/10 测试ArrayMap是否是有序的
        val arrayMap = ArrayMap<String,String>()
        arrayMap["1"] = "1"
        arrayMap["2"] = "2"
        arrayMap["77"] = "77"
        arrayMap["sss"] = "sss"
        arrayMap["34"] = "34"
        arrayMap["3"] = "3"
        arrayMap["56"] = "56"

        arrayMap.forEach {
            Log.i("ContentProviderStart1", "arraymap key : ${it.key} value : ${it.value}")
        }
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor? = null

    override fun getType(p0: Uri): String? = null

    override fun insert(p0: Uri, p1: ContentValues?): Uri? = null

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int = 0

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int = 0
}