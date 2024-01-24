package com.pumpkin.applets_container.data.source.bean

import com.google.gson.JsonObject
import com.pumpkin.data.db.entity.GameTable

class Entity(val id: String, val icon: String, val link: String, val name: String, val tag: String? = null, val description: String? = null, val extra: JsonObject? = null) {
    override fun toString(): String {
        return "Entity(id='$id', icon='$icon', link='$link', name='$name', tag=$tag, description=$description, extra=$extra)"
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