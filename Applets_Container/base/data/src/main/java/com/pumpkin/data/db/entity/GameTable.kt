package com.pumpkin.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.JsonObject
import com.pumpkin.data.db.GAME_TABLE_NAME


/**
 * Entity标签用于将SimpleStudent类与Room中的数据表对应起来。tableName属性可以为数据表设置表名，若不设置，则表名与类名相同
 * PrimaryKey标签用于指定该字段作为表的主键。 autoGenerate = true  Set to true to let SQLite generate the unique id.(设置为 true 让 SQLite 生成唯一的 id。)
 * ColumnInfo标签可用于设置该字段存储在数据库表中的名字，并指定字段的类型。
 * Ignore标签用来告诉Room忽略该字段或方法。
 */
@Entity(tableName = GAME_TABLE_NAME)
class GameTable(
    @PrimaryKey
    val id: String,
    val name: String,
    @ColumnInfo(defaultValue = "")
    val describe: String,
    val icon: String,
    @ColumnInfo(defaultValue = "", name = "big_icon")
    val bigIcon: String,
    val extra: JsonObject?,//额外参数
    @ColumnInfo(defaultValue = "", name = "module_id")
    val moduleId: String// 该数据被使用在那个模块
)