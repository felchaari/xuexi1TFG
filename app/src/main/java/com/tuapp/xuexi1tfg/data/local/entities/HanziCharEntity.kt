package com.tuapp.xuexi1tfg.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hanzi_chars")
data class HanziCharEntity(
    @PrimaryKey @ColumnInfo(name = "character") val character: String,
    @ColumnInfo(name = "traditional") val traditional: String?,
    @ColumnInfo(name = "pinyin") val pinyin: String?,
    @ColumnInfo(name = "meaning") val meaning: String?,
    @ColumnInfo(name = "difficulty") val difficulty: String?,
    @ColumnInfo(name = "type_of_word") val typeOfWord: String?,
    @ColumnInfo(name = "frequency") val frequency: String?,
    @ColumnInfo(name = "radicals_json") val radicalsJson: String?, // Se guardará como String en la DB
    @ColumnInfo(name = "stroke_order_visual_json") val strokeOrderVisualJson: String?,
    @ColumnInfo(name = "stroke_order_svg_json") val strokeOrderSVGJson: String?,
    @ColumnInfo(name = "stroke_count") val strokeCount: Int?,
    @ColumnInfo(name = "hsk_level") val hskLevel: Int?,
    @ColumnInfo(name = "is_favorite") val isFavorite: Boolean = false,
    @ColumnInfo(name = "last_reviewed") val lastReviewed: Long = 0L,
    @ColumnInfo(name = "next_review") val nextReview: Long = 0L,
    @ColumnInfo(name = "ease_factor") val easeFactor: Float = 2.5f,
    @ColumnInfo(name = "repetitions") val repetitions: Int = 0
)