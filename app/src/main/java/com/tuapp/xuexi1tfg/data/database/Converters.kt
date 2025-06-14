package com.tuapp.xuexi1tfg.data.database


import androidx.room.TypeConverter
import java.util.Date

/**
 * Convertidores de tipos para Room Database
 *
 * Room no puede almacenar directamente algunos tipos de datos,
 * por lo que necesitamos convertidores para transformarlos
 * a tipos que Room pueda manejar (primitivos, String, etc.)
 */
class Converters {

    /**
     * Convierte timestamp Long a Date
     */
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    /**
     * Convierte Date a timestamp Long
     */
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    /**
     * Convierte String separado por comas a List<String>
     * Útil si necesitas almacenar listas de strings
     */
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(",")
    }

    /**
     * Convierte String separado por comas a List<String>
     */
    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
    }

    /**
     * Convierte String separado por comas a List<Int>
     * Útil si necesitas almacenar listas de números
     */
    @TypeConverter
    fun fromIntList(value: List<Int>?): String? {
        return value?.joinToString(",")
    }

    /**
     * Convierte String separado por comas a List<Int>
     */
    @TypeConverter
    fun toIntList(value: String?): List<Int>? {
        return value?.split(",")?.mapNotNull {
            it.trim().toIntOrNull()
        }
    }
}