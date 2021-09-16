package com.zxf.jetpackrelated.room.baseUse

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 作者： zxf
 * 描述： Student Entity 类
 */

/**
 * Entity标签用于将SimpleStudent类与Room中的数据表对应起来。tableName属性可以为数据表设置表名，若不设置，则表名与类名相同
 * PrimaryKey标签用于指定该字段作为表的主键。 autoGenerate = true  Set to true to let SQLite generate the unique id.(设置为 true 让 SQLite 生成唯一的 id。)
 * ColumnInfo标签可用于设置该字段存储在数据库表中的名字，并指定字段的类型。
 * Ignore标签用来告诉Room忽略该字段或方法。
 */
@Entity(tableName = SIMPLE_STUDENT_TABLE_NAME)
data class SimpleStudentEntity(

    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(
        name = SIMPLE_STUDENT_TABLE_STUDENT_ID,
        typeAffinity = ColumnInfo.INTEGER
    ) val id: Int = 0,//该值设置为自增的主键，所以默认会在数据库中自增，0，1，2，3。。。

    @NonNull
    @ColumnInfo(name = SIMPLE_STUDENT_TABLE_STUDENT_NAME, typeAffinity = ColumnInfo.TEXT)
    val name: String?,

    @NonNull
    @ColumnInfo(name = SIMPLE_STUDENT_TABLE_STUDENT_AGE, typeAffinity = ColumnInfo.TEXT)
    val age: String?
)


/**
 * 表名字相关，统一定义.避免互相引用
 */
const val SIMPLE_STUDENT_TABLE_NAME = "simple_student"
const val SIMPLE_STUDENT_TABLE_STUDENT_ID = "student_id"
const val SIMPLE_STUDENT_TABLE_STUDENT_NAME = "student_name"
const val SIMPLE_STUDENT_TABLE_STUDENT_AGE = "student_age"
