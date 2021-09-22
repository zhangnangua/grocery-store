package com.zxf.jetpackrelated.room.liveDataOrFlow.keyValueTest

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 作者： zxf
 * 描述：keyValueEntity
 */

@Entity(tableName = KEY_VALUE_TABLE)
data class KeyValueEntity(
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = KEY_VALUE_TABLE_KEY)
    val key: String,

    @NonNull
    @ColumnInfo(name = KEY_VALUE_TABLE_VALUE)
    val value: String?
)

const val KEY_VALUE_TABLE = "key_value_table"
const val KEY_VALUE_TABLE_KEY = "key"
const val KEY_VALUE_TABLE_VALUE = "value"
