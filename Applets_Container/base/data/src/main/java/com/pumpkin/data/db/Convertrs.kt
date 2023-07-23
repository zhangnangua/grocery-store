package com.pumpkin.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject

class JsonObjectConverters {
    @TypeConverter
    fun jsonObjectToString(value: JsonObject?): String? =
        value?.toString()

    @TypeConverter
    fun stringToJsonObject(value: String?): JsonObject? =
        value?.let {
            try {
                Gson().fromJson(it, JsonObject::class.java)
            } catch (e: Exception) {
                null
            }
        }
}