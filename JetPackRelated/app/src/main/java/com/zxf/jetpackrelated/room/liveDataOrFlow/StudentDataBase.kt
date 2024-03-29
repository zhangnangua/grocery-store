package com.zxf.jetpackrelated.room.liveDataOrFlow

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.zxf.jetpackrelated.room.liveDataOrFlow.flow.StudentFlowDao
import com.zxf.jetpackrelated.room.liveDataOrFlow.liveData.StudentLiveDao
import com.zxf.jetpackrelated.room.liveDataOrFlow.migration.*
import com.zxf.utils.AppUtil

/**
 * 作者： zxf
 * 描述： 数据库
 */
@Database(entities = arrayOf(StudentEntity::class, FruitEntity::class), version = 4)
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
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANT = it
                    }
            }
        }

        /**
         * 数据库升级 1 到 2
         */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //新增 FRUIT 表
                database.execSQL("CREATE TABLE IF NOT EXISTS `$FRUIT_TABLE_NAME` (`$FRUIT_TABLE_ID` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `$FRUIT_TABLE_TEXT` TEXT)")
            }
        }

        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //FRUIT 表  新增一列
                database.execSQL("ALTER TABLE `$FRUIT_TABLE_NAME` ADD COLUMN `$FRUIT_TABLE_OTHER_NAME` TEXT ")
            }
        }

        /**
         * 销毁与重建策略
         * 在Sqlite中修改表结构比较麻烦。
         * 例如，我们想将Student表中的age字段类型从INTEGER改为TEXT。同时新增一列student_add_column。
         */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `temp_student` (`student_id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `student_name` TEXT, `student_age` TEXT, `student_add_column` TEXT DEFAULT 'default') ")
                database.execSQL("INSERT INTO temp_student (student_id,student_name,student_age) SELECT student_id , student_name ,student_age FROM student")
                database.execSQL("DROP TABLE student")
                database.execSQL("ALTER TABLE temp_student RENAME TO student")
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

    /**
     * 获取ConflateEntityDao
     */
    abstract fun getConflateEntityDao(): ConflateDao

}