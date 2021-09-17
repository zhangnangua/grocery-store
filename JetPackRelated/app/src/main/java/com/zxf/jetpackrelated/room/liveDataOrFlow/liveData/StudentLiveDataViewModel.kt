package com.zxf.jetpackrelated.room.liveDataOrFlow.liveData

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.zxf.jetpackrelated.room.liveDataOrFlow.StudentDataBase
import com.zxf.jetpackrelated.room.liveDataOrFlow.StudentEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * 作者： zxf
 * 描述： liveData ViewModel
 */
class StudentLiveDataViewModel : ViewModel() {

    /**
     * liveData dao
     */
    private var _dao: StudentLiveDao? = StudentDataBase.getDataBase().getStudentLiveDao()
    private val dao: StudentLiveDao
        get() = _dao!!

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

    fun obtainStudentByNameUseLiveData(name: String): LiveData<List<StudentEntity>> {
        return dao.obtainStudentByName(name)
    }

    fun obtainStudentAllUseLiveData(): LiveData<List<StudentEntity>> {
        return dao.obtainStudentAll()
    }

    override fun onCleared() {
        super.onCleared()
        _dao = null
    }
}