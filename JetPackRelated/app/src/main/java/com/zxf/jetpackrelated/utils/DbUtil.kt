package com.zxf.jetpackrelated.utils

import android.text.TextUtils
import androidx.annotation.NonNull
import androidx.room.withTransaction
import com.zxf.jetpackrelated.room.liveDataOrFlow.keyValueTest.KeyValueDatabase
import com.zxf.jetpackrelated.room.liveDataOrFlow.keyValueTest.KeyValueEntity

/**
 * 作者： zxf
 * 描述： key-value util
 */
object DbUtil {

    private val keyValueDb = KeyValueDatabase.getDatabase()

    private val keyValueDao = keyValueDb.getKeyValueDao()

    /**
     * 写入缓存
     */
    suspend fun writeCache(@NonNull key: String, @NonNull    value: String?) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(value)) {
            return
        }
        keyValueDao.insertOrReplace(KeyValueEntity(key, value))
    }

    /**
     * 使用事务处理
     */
    suspend fun <T> withTransaction(block: suspend () -> T): T {
        return keyValueDb.withTransaction {
            block()
        }
    }

    /**
     * 获取缓存
     */
    suspend fun obtainValue(@NonNull key: String):String? {
        return keyValueDao.selectValue(key)
    }
}