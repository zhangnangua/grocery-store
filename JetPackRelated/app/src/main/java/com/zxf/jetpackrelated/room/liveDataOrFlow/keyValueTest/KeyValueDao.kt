package com.zxf.jetpackrelated.room.liveDataOrFlow.keyValueTest

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * 作者： zxf
 * 描述： keyValueDao
 */
@Dao
interface KeyValueDao {

    /**
     * 如果重复则替换
     * 默认以唯一约束（unique）作为判断依据，底层的sql语句为INSERT OR REPLACE
     * 主键是PRIMARY KEY也是unique  区别是  PRIMARY KEY不允许为null,unique可以为null
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(keyValueEntity: KeyValueEntity)

    @Query("SELECT $KEY_VALUE_TABLE_VALUE FROM $KEY_VALUE_TABLE WHERE $KEY_VALUE_TABLE_KEY = :key LIMIT 1")
    suspend fun selectValue(@NonNull key: String) : String?
}