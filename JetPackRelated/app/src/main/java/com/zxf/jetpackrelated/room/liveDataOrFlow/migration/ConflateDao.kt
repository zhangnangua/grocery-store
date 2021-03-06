package com.zxf.jetpackrelated.room.liveDataOrFlow.migration

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * 作者： zxf
 * 描述： 描述
 */
@Dao
interface ConflateDao {
    @Query("select * from $FRUIT_TABLE_NAME")
    suspend fun obtainFruit() : List<FruitEntity>

    @Insert
    suspend fun insertFruit(fruitEntity: FruitEntity)

    /**
     * 如果重复则替换
     * 默认以唯一约束（unique）作为判断依据，底层的sql语句为INSERT OR REPLACE
     * 主键是PRIMARY KEY也是unique  区别是  PRIMARY KEY不允许为null,unique可以为null
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConflict(fruitEntity: FruitEntity)
}