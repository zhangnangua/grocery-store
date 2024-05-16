package com.pumpkin.pac.bean

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.mvvm.util.Constant
import com.pumpkin.mvvm.util.Event
import com.pumpkin.webCache.util.MD5Utils

/**
 * id 、name、icon is required parameters.
 */
class GameEntity constructor(
    val id: String,// id  todo 默认没有id，考虑使用url 转换成md5
    val name: String,// name
    val link: String,//link
    val describe: String = "",// 描述
    val icon: String,// 游戏icon
    val bigIcon: String = "",// 大图
    val extra: JsonObject? = null, // 额外的参数
    val tag: String?,
    val orientation: Int? = Constant.INVALID_ID
) : Parcelable, Event {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
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
        },
        parcel.readString(),
        parcel.readInt()
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(link)
        dest.writeString(describe)
        dest.writeString(icon)
        dest.writeString(bigIcon)
        dest.writeString(extra?.toString())
        dest.writeString(tag)
        dest.writeInt(orientation ?: Constant.INVALID_ID)
    }

    override fun toString(): String {
        return "GameEntity(id='$id', name='$name', link='$link', describe='$describe', icon='$icon', bigIcon='$bigIcon', extra=$extra , tag=$tag , orientation=$orientation)"
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

fun gameIdByUrl(url: String): String = MD5Utils.md5(url)

///region 转换
fun GameEntity.entityToTableMd5(moduleId: String) = GameTable(
    id = MD5Utils.md5(this.id + moduleId),
    name = this.name,
    link = this.link,
    describe = this.describe,
    icon = this.icon,
    bigIcon = this.bigIcon,
    extra = this.extra,
    moduleId = moduleId,
    tag = tag
)

fun GameEntity.entityToTable(moduleId: String) = GameTable(
    id = this.id,
    name = this.name,
    link = this.link,
    describe = this.describe,
    icon = this.icon,
    bigIcon = this.bigIcon,
    extra = this.extra,
    moduleId = moduleId,
    tag = this.tag
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

fun List<GameEntity?>.entityToTablesMd5(moduleId: String): List<GameTable> {
    val tables = mutableListOf<GameTable>()
    forEach {
        if (it != null) {
            tables.add(it.entityToTableMd5(moduleId))
        }
    }
    return tables
}

fun GameTable.tableToEntity() = GameEntity(
    id = this.id,
    name = this.name,
    link = this.link,
    describe = this.describe,
    icon = this.icon,
    bigIcon = this.bigIcon,
    extra = this.extra,
    tag = this.tag
)


fun GameResponse.responseToEntity() = GameEntity(
    id = this.id ?: "",
    name = this.name ?: "",
    link = this.link ?: "",
    describe = this.describe ?: "",
    icon = this.icon ?: "",
    bigIcon = this.bigIcon ?: "",
    extra = this.extra,
    tag = this.tag,
    orientation = this.orientation
)


fun List<GameTable?>.tablesToEntities(): ArrayList<GameEntity> {
    val tables = ArrayList<GameEntity>()
    forEach {
        if (it != null) {
            tables.add(it.tableToEntity())
        }
    }
    return tables
}
///endregion