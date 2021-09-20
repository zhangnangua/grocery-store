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

        //禁止指令重排序
        @Volatile
        private var INSTANT: StudentDataBase? = null

        /**
         * 双重锁单例
         *  fallbackToDestructiveMigration 此方法会指示 Room 在需要执行没有定义迁移路径的增量迁移时，
         *  破坏性地重新创建应用的数据库表。不会抛出IllegalStateException
         */
        fun getDataBase(): StudentDataBase {
            return INSTANT ?: synchronized(this) {
                INSTANT ?: Room.databaseBuilder(
                    AppUtil.application,
                    StudentDataBase::class.java,
                    STUDENT_DB_NAME
                ).fallbackToDestructiveMigration()
                    .build()
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