package com.zxf.jetpackrelated.room.baseUse

import androidx.room.*

/**
 * 作者： zxf
 * 描述： 定义Dao,定义增删改查方法
 */
@Dao
interface SimpleStudentDao {

    @Insert
    fun insertStudent(studentEntity: SimpleStudentEntity)

    @Insert
    fun insertStudentAll(studentEntity: List<SimpleStudentEntity>)

    @Delete
    fun deleteStudent(studentEntity: SimpleStudentEntity)

    @Update
    fun updateStudent(studentEntity: SimpleStudentEntity)

    @Query("select * from $SIMPLE_STUDENT_TABLE_NAME")
    fun getStudentAll(): List<SimpleStudentEntity>

    @Query("select * from $SIMPLE_STUDENT_TABLE_NAME where $SIMPLE_STUDENT_TABLE_STUDENT_ID = :id")
    fun getStudentById(id: Int): List<SimpleStudentEntity>

    @Insert
    suspend fun suspendInsertStudent(studentEntity: SimpleStudentEntity)

    @Query("select * from $SIMPLE_STUDENT_TABLE_NAME where $SIMPLE_STUDENT_TABLE_STUDENT_NAME = :name")
    suspend fun suspendStudentByName(name: String): List<SimpleStudentEntity>

}