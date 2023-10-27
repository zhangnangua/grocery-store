package com.pumpkin.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.data.db.entity.RecentlyGameTable

@Dao
interface RecentlyGameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(table: RecentlyGameTable)

    @Query("select * from game_table LEFT JOIN recently_game_table WHERE game_table.id = recently_game_table.id  order by recently_game_table.time DESC limit :num")
    fun obtainGame(num: Int): List<GameTable>
}