package com.pumpkin.applets_container.data.source.bean

import com.google.gson.JsonObject
import com.pumpkin.data.db.entity.GameTable
import com.pumpkin.pac.bean.GameEntity

class Entity(val id: String, val icon: String, val link: String, val name: String, val tag: String? = null, val description: String? = null, val extra: JsonObject? = null) {
    override fun toString(): String {
        return "Entity(id='$id', icon='$icon', link='$link', name='$name', tag=$tag, description=$description, extra=$extra)"
    }

    fun toGameEntity(): GameEntity {
        return GameEntity(id = this.id,
            name = this.name,
            link = this.link,
            describe = this.description ?: "",
            icon = this.icon,
            bigIcon = "",
            extra = this.extra,
            tag = this.tag)
    }

    fun toGameTable(moduleId: String): GameTable {
        return GameTable(
            id = id,
            name = name,
            link = link,
            describe = description ?: "",
            icon = icon,
            bigIcon = "",
            extra = null,
            moduleId = moduleId,
            tag = tag)
    }
}