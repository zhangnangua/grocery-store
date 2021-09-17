package com.zxf.jetpackrelated.room.liveDataOrFlow.liveData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.zxf.jetpackrelated.room.liveDataOrFlow.STUDENT_TABLE_NAME
import com.zxf.jetpackrelated.room.liveDataOrFlow.STUDENT_TABLE_STUDENT_NAME
import com.zxf.jetpackrelated.room.liveDataOrFlow.StudentEntity

/**
 * 作者： zxf
 * 描述： liveDao
 */
@Dao
interface StudentLiveDao{
    @Insert
    suspend fun insertStudent(student: StudentEntity)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("select * from $STUDENT_TABLE_NAME where $STUDENT_TABLE_STUDENT_NAME = :name")
    fun obtainStudentByName(name: String): LiveData<List<StudentEntity>>

    @Query("select * from $STUDENT_TABLE_NAME")
    fun obtainStudentAll(): LiveData<List<StudentEntity>>
}