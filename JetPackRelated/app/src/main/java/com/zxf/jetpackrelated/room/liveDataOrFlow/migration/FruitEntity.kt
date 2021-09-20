package com.zxf.jetpackrelated.room.liveDataOrFlow.migration

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 作者： zxf
 * 描述： 水果实体，migration数据库表测试
 */
@Entity(tableName = FRUIT_TABLE_NAME)
data class FruitEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = FRUIT_TABLE_ID)
    val id: Int = 0,
    @ColumnInfo(name = FRUIT_TABLE_TEXT)
    val text: String?,
    @ColumnInfo(name = FRUIT_TABLE_OTHER_NAME)
    val text2: String?
)

/**
 * 表名字相关，统一定义.避免互相引用
 */
const val FRUIT_TABLE_NAME = "fruit"
const val FRUIT_TABLE_ID = "fruit_id"
const val FRUIT_TABLE_TEXT = "fruit_name"
const val FRUIT_TABLE_OTHER_NAME = "fruit_other_name"