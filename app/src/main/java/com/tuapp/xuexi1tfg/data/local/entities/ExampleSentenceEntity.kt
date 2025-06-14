package com.tuapp.xuexi1tfg.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "example_sentences")
data class ExampleSentenceEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "chinese_simplified") val chineseSimplified: String,
    @ColumnInfo(name = "chinese_traditional") val chineseTraditional: String?,
    @ColumnInfo(name = "pinyin") val pinyin: String?,
    @ColumnInfo(name = "translation") val translation: String
)