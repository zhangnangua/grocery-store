package com.zxf.jetpackrelated.room.baseUse

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zxf.utils.AppUtil

/**
 * 作者： zxf
 * 描述： 创建数据库
 */

/**
 * Database标签用于告诉系统这是Room数据库对象。
 * entities属性用于指定该数据库有哪些表，若需要建立多张表，则表名以逗号相隔开。
 * version属性用于指定数据库版本号，后面数据库的升级正是依据版本号进行判断的。
 *
 *
 * 数据库类需要继承自RoomDatabase，并通过Room.databaseBuilder()结合单例设计模式完成创建。
 * 另外，我们之前创建的Dao对象，在此以抽象方法的形式返回，只需一行代码即可。
 */
@Database(entities = arrayOf(SimpleStudentEntity::class), version = 1)
abstract class SimpleMyDataBase : RoomDatabase() {

    companion object {
        /**
         * 使用const val修饰在直接以静态字段形式初始化（编译时常量）
         */
        private const val DATA_NAME = "simple_db"

        /**
         * 双重校验锁单例
         * 对应java的静态变量
         * 单独使用val修饰，在静态代码块中初始化（运行时常量）
         */
        @Volatile
        private var INSTANCE: SimpleMyDataBase? = null

        /**
         * 返回数据库实例
         */
        fun getDataBase(): SimpleMyDataBase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room
                .databaseBuilder(AppUtil.application, SimpleMyDataBase::class.java, DATA_NAME)
                .build().also {
                    INSTANCE = it
                }
        }
    }

    /**
     * 返回 SimpleStudentDao Dao对象
     */
    abstract fun simpleStudentDao(): SimpleStudentDao

}