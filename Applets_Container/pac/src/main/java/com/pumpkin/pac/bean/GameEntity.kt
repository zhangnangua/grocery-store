package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pumpkin.data.db.entity.GameTable

/**
 * id 、name、icon is required parameters.
 */
class GameEntity(
    val id: String,// id  todo 默认没有id，考虑使用url 转换成md5
    val name: String,// name
    val describe: String = "",// 描述
    val icon: String,// 游戏icon
    val bigIcon: String = "",// 大图
    val extra: JsonObject? = null // 额外的参数
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        try {
            if (parcel.readString() == null) {
                null
            } else {
                Gson().fromJson(parcel.readString(), JsonObject::class.java)
            }
        } catch (e: Exception) {
            null
        }
    ) {
    }

    ///region 转换
    fun GameEntity.entityToTable(moduleId: String) = GameTable(
        id = this.id,
        name = this.name,
        describe = this.describe,
        icon = this.icon,
        bigIcon = this.bigIcon,
        extra = this.extra,
        moduleId = moduleId
    )

    fun List<GameEntity?>.entityToTables(moduleId: String): List<GameTable> {
        val tables = mutableListOf<GameTable>()
        forEach {
            if (it != null) {
                tables.add(it.entityToTable(moduleId))
            }
        }
        return tables
    }

    fun GameTable.tableToEntity() = GameEntity(
        id = this.id,
        name = this.name,
        describe = this.describe,
        icon = this.icon,
        bigIcon = this.bigIcon,
        extra = this.extra
    )


    fun List<GameTable?>.tablesToEntities(): List<GameEntity> {
        val tables = mutableListOf<GameEntity>()
        forEach {
            if (it != null) {
                tables.add(it.tableToEntity())
            }
        }
        return tables
    }
    ///endregion

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        if (dest != null) {
            dest.writeString(id)
            dest.writeString(name)
            dest.writeString(describe)
            dest.writeString(icon)
            dest.writeString(bigIcon)
            dest.writeString(extra?.toString())
        }
    }

    companion object CREATOR : Parcelable.Creator<GameEntity> {
        override fun createFromParcel(parcel: Parcel): GameEntity {
            return GameEntity(parcel)
        }

        override fun newArray(size: Int): Array<GameEntity?> {
            return arrayOfNulls(size)
        }
    }
}