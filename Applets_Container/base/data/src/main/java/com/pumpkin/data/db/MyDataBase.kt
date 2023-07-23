package com.pumpkin.data.db

import android.app.Application
import androidx.room.*
import com.pumpkin.data.JsonObjectConverters
import com.pumpkin.data.db.dao.GameDao
import com.pumpkin.data.db.entity.GameTable

@Database(entities = [GameTable::class], version = 1)
@TypeConverters(JsonObjectConverters::class)
abstract class MyDataBase : RoomDatabase() {
    companion object {

        /**
         * 使用const val修饰在直接以静态字段形式初始化（编译时常量）
         */
        private const val DATA_NAME = "pac_db"

        /**
         * 双重校验锁单例
         * 对应java的静态变量
         * 单独使用val修饰，在静态代码块中初始化（运行时常量）
         */
        @Volatile
        private var INSTANCE: MyDataBase? = null

        /**
         * 返回数据库实例
         */
        fun getDataBase(application: Application): MyDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room
                .databaseBuilder(application, MyDataBase::class.java, DATA_NAME)
                .build()
                .also {
                    INSTANCE = it
                }
        }
    }

    abstract fun gameDao(): GameDao
}