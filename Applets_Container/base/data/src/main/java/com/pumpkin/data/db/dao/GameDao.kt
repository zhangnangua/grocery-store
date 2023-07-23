package com.pumpkin.data.db.dao

import androidx.room.*
import com.pumpkin.data.db.GAME_TABLE_NAME
import com.pumpkin.data.db.entity.GameTable
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: GameTable)

    @Delete
    suspend fun delete(table: GameTable)


    @Query("select * from $GAME_TABLE_NAME where id = :id")
    fun obtainGameById(id: String): Flow<List<GameTable>>
}