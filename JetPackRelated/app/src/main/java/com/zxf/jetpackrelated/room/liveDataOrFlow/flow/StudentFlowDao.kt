package com.zxf.jetpackrelated.room.liveDataOrFlow.flow

import androidx.room.*
import com.zxf.jetpackrelated.room.liveDataOrFlow.STUDENT_TABLE_NAME
import com.zxf.jetpackrelated.room.liveDataOrFlow.STUDENT_TABLE_STUDENT_NAME
import com.zxf.jetpackrelated.room.liveDataOrFlow.StudentEntity
import kotlinx.coroutines.flow.Flow

/**
 * 作者： zxf
 * 描述： flowDao
 */
@Dao
interface StudentFlowDao{
    @Insert
    suspend fun insertStudent(student: StudentEntity)

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)

    @Query("select * from $STUDENT_TABLE_NAME where $STUDENT_TABLE_STUDENT_NAME = :name")
     fun obtainStudentByName(name: String): Flow<List<StudentEntity>>

    @Query("select * from $STUDENT_TABLE_NAME")
     fun obtainStudentAll(): Flow<List<StudentEntity>>
}