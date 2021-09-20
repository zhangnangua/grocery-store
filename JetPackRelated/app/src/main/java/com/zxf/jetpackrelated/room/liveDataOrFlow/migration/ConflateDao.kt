package com.zxf.jetpackrelated.room.liveDataOrFlow.migration

import androidx.room.Dao
import androidx.room.Insert
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
}