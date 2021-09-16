package com.zxf.jetpackrelated.room.liveDataOrFlow

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 作者： zxf
 * 描述： Entity
 */
@Entity
data class StudentEntity(
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(
        name = STUDENT_TABLE_STUDENT_ID,
        typeAffinity = ColumnInfo.INTEGER
    ) val id: Int = 0,//该值设置为自增的主键，所以默认会在数据库中自增，0，1，2，3。。。

    @NonNull
    @ColumnInfo(name = STUDENT_TABLE_STUDENT_NAME, typeAffinity = ColumnInfo.TEXT)
    val name: String?,

    @NonNull
    @ColumnInfo(name = STUDENT_TABLE_STUDENT_AGE, typeAffinity = ColumnInfo.TEXT)
    val age: String?
)

/**
 * 表名字相关，统一定义.避免互相引用
 */
const val STUDENT_TABLE_NAME = "simple_student"
const val STUDENT_TABLE_STUDENT_ID = "student_id"
const val STUDENT_TABLE_STUDENT_NAME = "student_name"
const val STUDENT_TABLE_STUDENT_AGE = "student_age"