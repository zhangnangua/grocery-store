package com.zxf.jetpackrelated.room.liveDataOrFlow

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentFlowDao
import com.zxf.jetpackrelated.room.liveDataOrFlow.liveData.StudentLiveDao
import com.zxf.jetpackrelated.utils.AppUtil

/**
 * 作者： zxf
 * 描述： 数据库
 */
@Database(entities = arrayOf(StudentEntity::class), version = 1)
abstract class StudentDataBase : RoomDatabase() {

    companion object {

        private var INSTANT: StudentDataBase? = null

        /**
         * 双重锁单例
         */
        fun getDataBase(): StudentDataBase {
            return INSTANT ?: synchronized(this) {
                INSTANT ?: Room.databaseBuilder(
                    AppUtil.application,
                    StudentDataBase::class.java,
                    STUDENT_TABLE_NAME
                ).build()
                    .also {
                        INSTANT = it
                    }
            }
        }
    }

    /**
     *获取StudentLiveDao
     */
    abstract fun getStudentFlowDao(): StudentFlowDao

    /**
     * 获取StudentLiveDao
     */
    abstract fun getStudentLiveDao(): StudentLiveDao

}