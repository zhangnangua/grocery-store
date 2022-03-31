package com.zxf.jetpackrelated.room.liveDataOrFlow.flow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zxf.jetpackrelated.room.liveDataOrFlow.StudentEntity
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

    fun obtainStudentByNameUseFlow(name: String): Flow<List<StudentEntity>> {
        return dao.obtainStudentByName(name)
    }

    fun obtainStudentAllUseFlow() = dao.obtainStudentAll()
}

class StudentFactory(private val dao: StudentFlowDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StudentViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StudentViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown viewModel class. ")

//        第二种实例化的方式
//        modelClass.getConstructor(StudentFlowDao::class.java).newInstance(dao)

    }

}