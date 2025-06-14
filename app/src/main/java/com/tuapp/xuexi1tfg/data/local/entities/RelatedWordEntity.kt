package com.tuapp.xuexi1tfg.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "related_words")
data class RelatedWordEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "simplified") val simplified: String,
    @ColumnInfo(name = "traditional") val traditional: String?,
    @ColumnInfo(name = "pinyin") val pinyin: String?,
    @ColumnInfo(name = "meaning") val meaning: String?,
    @ColumnInfo(name = "type_of_word") val typeOfWord: String?
)