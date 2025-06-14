package com.tuapp.xuexi1tfg.data.local.db


import androidx.room.TypeConverter
import com.google.gson.Gson // Necesitas esta importación para Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.json.Json // Necesitas esta importación para Kotlinx Serialization
import kotlinx.serialization.json.JsonElement // Y esta para JsonElement

class Converters {

    private val gson = Gson()
    private val json = Json { ignoreUnknownKeys = true }

   /*  @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    */

    @TypeConverter
    fun fromJsonElement(jsonElement: JsonElement?): String? {
        return jsonElement?.let { json.encodeToString(JsonElement.serializer(), it) }
    }

    @TypeConverter
    fun toActualJsonElement(jsonString: String?): JsonElement? {
        return jsonString?.let { json.parseToJsonElement(it) }
    }
}