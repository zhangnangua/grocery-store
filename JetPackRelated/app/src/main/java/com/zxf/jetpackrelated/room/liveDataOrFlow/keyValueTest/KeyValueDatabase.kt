package com.zxf.jetpackrelated.room.liveDataOrFlow.keyValueTest

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zxf.jetpackrelated.utils.AppUtil

/**
 * 作者： zxf
 * 描述： 描述
 */
@Database(entities = arrayOf(KeyValueEntity::class), version = 1)
internal abstract class KeyValueDatabase : RoomDatabase() {

    companion object {
        /**
         * 数据库名字
         */
        private const val KEY_VALUE_DATABASE = "key_value_db"

        @Volatile
        private var INSTANCE: KeyValueDatabase? = null

        fun getDatabase(): KeyValueDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    AppUtil.application,
                    KeyValueDatabase::class.java,
                    KEY_VALUE_DATABASE
                ).build()
                    .also {
                        INSTANCE = it
                    }
            }
        }
    }

    abstract fun getKeyValueDao(): KeyValueDao
}