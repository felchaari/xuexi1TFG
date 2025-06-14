package com.tuapp.xuexi1tfg.data.local.db

import android.content.Context
import com.tuapp.xuexi1tfg.data.local.entities.ExampleSentenceEntity
import com.tuapp.xuexi1tfg.data.local.entities.HanziCharEntity // Añadido
import com.tuapp.xuexi1tfg.data.local.entities.RelatedWordEntity // Añadido
import com.tuapp.xuexi1tfg.data.model.ChineseData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.InputStreamReader
import java.lang.Exception
import com.tuapp.xuexi1tfg.data.local.dao.HanziCharDao
import com.tuapp.xuexi1tfg.data.local.dao.ExampleSentenceDao
import com.tuapp.xuexi1tfg.data.local.dao.RelatedWordDao

class DatabasePrepopulate {

    companion object {
        private val jsonParser = Json {
            ignoreUnknownKeys = true
            isLenient = true
            allowStructuredMapKeys = true
        }

        // CORREGIDO: Marcar populateDatabase como 'suspend'
        // CORREGIDO: Eliminar el 'CoroutineScope().launch' interno,
        //            la coroutine se lanzará desde el punto de llamada (AppDatabase).
        suspend fun populateDatabase(context: Context, database: AppDatabase) { // <-- ¡Aquí el cambio!
            val characterDao = database.hanziCharDao()
            val wordDao = database.relatedWordDao()
            val exampleSentenceDao = database.exampleSentenceDao()

            try {
                val inputStream = context.assets.open("all_data538.json")
                val reader = InputStreamReader(inputStream, "UTF-8")
                val jsonData = reader.readText()
                reader.close()
                inputStream.close()

                val chineseData: ChineseData = jsonParser.decodeFromString(ChineseData.serializer(), jsonData)

                val characterEntities = chineseData.hanzi.map { hanziChar ->
                    HanziCharEntity(
                        character = hanziChar.character,
                        traditional = hanziChar.traditional,
                        pinyin = hanziChar.pinyin,
                        meaning = hanziChar.meaning,
                        difficulty = hanziChar.difficulty,
                        typeOfWord = hanziChar.typeOfWord,
                        frequency = hanziChar.frequency,
                        radicalsJson = hanziChar.radicalsJson?.let { jsonParser.encodeToString(JsonElement.serializer(), it) },
                        strokeOrderVisualJson = hanziChar.strokeOrderVisualJson?.let { jsonParser.encodeToString(JsonElement.serializer(), it) },
                        strokeOrderSVGJson = hanziChar.strokeOrderSVGJson?.let { jsonParser.encodeToString(JsonElement.serializer(), it) },
                        strokeCount = hanziChar.strokeCount,
                        hskLevel = hanziChar.hskLevel,
                        isFavorite = hanziChar.isFavorite,
                        lastReviewed = 0L,
                        nextReview = 0L,
                        easeFactor = 2.5f,
                        repetitions = 0
                    )
                }

                val wordEntities = chineseData.related_words.map { relatedWord ->
                    RelatedWordEntity(
                        simplified = relatedWord.simplified,
                        traditional = relatedWord.traditional,
                        pinyin = relatedWord.pinyin,
                        meaning = relatedWord.meaning,
                        typeOfWord = relatedWord.typeOfWord
                    )
                }

                val exampleSentenceEntities = chineseData.example_sentences.map { exampleSentence ->
                    ExampleSentenceEntity(
                        chineseSimplified = exampleSentence.chineseSimplified,
                        chineseTraditional = exampleSentence.chineseTraditional,
                        pinyin = exampleSentence.pinyin,
                        translation = exampleSentence.translation
                    )
                }

                characterDao.insertAll(characterEntities)
                wordDao.insertAll(wordEntities)
                exampleSentenceDao.insertAll(exampleSentenceEntities)

                println("Base de datos precargada exitosamente con ${characterEntities.size} caracteres, ${wordEntities.size} palabras, ${exampleSentenceEntities.size} oraciones.")

            } catch (e: Exception) {
                println("Error precargando la base de datos: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}