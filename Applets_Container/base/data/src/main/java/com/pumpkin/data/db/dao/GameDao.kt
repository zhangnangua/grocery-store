package com.pumpkin.data.db.dao

import androidx.room.*
import com.pumpkin.data.db.GAME_TABLE_NAME
import com.pumpkin.data.db.entity.GameTable

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: GameTable)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOrIgnore(table: GameTable)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplaceGameList(list: List<GameTable?>?)

    @Delete
    suspend fun delete(table: GameTable)

    @Query("select * from $GAME_TABLE_NAME where id = :id")
    fun obtainGameById(id: String): GameTable?

    @Query("select * from $GAME_TABLE_NAME where id not in(:ids) order by random() limit :num")
    fun obtainGameByRandomExclude(num: Int, ids: List<String>): List<GameTable>

    @Query("select * from $GAME_TABLE_NAME order by random() limit :num")
    fun obtainGameByRandom(num: Int): List<GameTable>
}