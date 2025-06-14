package com.tuapp.xuexi1tfg.data.model



import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement // ¡Esta es una de las importaciones que te faltan!

@Serializable
data class ChineseData(
    val hanzi: List<HanziChar>,
    val related_words: List<RelatedWord>,
    val example_sentences: List<ExampleSentence>
)

@Serializable
data class HanziChar(
    val character: String,
    val traditional: String?,
    val pinyin: String?,
    val meaning: String?,
    val difficulty: String?,
    val typeOfWord: String?,
    val frequency: String?,
    val radicalsJson: JsonElement?, // Importante: JsonElement para el JSON anidado
    val strokeOrderVisualJson: JsonElement?,
    val strokeOrderSVGJson: JsonElement?,
    val strokeCount: Int?,
    val hskLevel: Int?,
    val isFavorite: Boolean = false
)

@Serializable
data class RelatedWord(
    val simplified: String,
    val traditional: String?,
    val pinyin: String?,
    val meaning: String?,
    val typeOfWord: String?
)

@Serializable
data class ExampleSentence(
    val chineseSimplified: String,
    val chineseTraditional: String?,
    val pinyin: String?,
    val translation: String
)