package com.pumpkin.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.pumpkin.data.db.RECENTLY_GAME_TABLE_NAME

@Entity(tableName = RECENTLY_GAME_TABLE_NAME)
class RecentlyGameTable(
    @PrimaryKey
    val id: String,
    val time: Long
) {
    override fun toString(): String {
        return "RecentlyGameTable(id='$id', time=$time)"
    }
}