package com.zxf.jetpackrelated.room.liveDataOrFlow

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentFlowDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * 作者： zxf
 * 描述： viewModel
 */
class StudentViewModel(private val dao: StudentFlowDao) : ViewModel() {
    suspend fun insertStudent(studentEntity: StudentEntity) {
        withContext(Dispatchers.IO) {
            dao.insertStudent(studentEntity)
        }
    }

    suspend fun updateStudent(studentEntity: StudentEntity) {
        withContext(Dispatchers.IO) {
            dao.updateStudent(studentEntity)
        }
    }

    suspend fun deleteStudent(studentEntity: StudentEntity) {
        withContext(Dispatchers.IO) {
            dao.deleteStudent(studentEntity)
        }
    }

//    suspend fun obtainStudentByNameUseFlow(name: String): Flow<List<StudentEntity>> {
//        return withContext(Dispatchers.IO) {
//            dao.obtainStudentByName(name) as Flow<List<StudentEntity>>
//        }
//    }
}

class StudentFactory(private val dao: StudentFlowDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class. ")
    }

}