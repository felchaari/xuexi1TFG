package com.tuapp.xuexi1tfg

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.withTransaction // ¡Importante! Asegúrate de tener esta importación
import com.tuapp.xuexi1tfg.data.local.db.AppDatabase
import com.tuapp.xuexi1tfg.data.local.entities.HanziCharEntity
import com.tuapp.xuexi1tfg.data.local.entities.ExampleSentenceEntity
import com.tuapp.xuexi1tfg.data.local.entities.RelatedWordEntity
import com.tuapp.xuexi1tfg.data.model.ChineseData
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.IOException

@HiltAndroidApp
class XuexiApplication : Application() {

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: AppDatabase by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "chinese_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("XuexiApplication", "Aplicación iniciada")

        applicationScope.launch {
            try {
                populateDatabaseIfNeeded()
            } catch (e: Exception) {
                Log.e("XuexiApplication", "Error al poblar la base de datos", e)
            }
        }
    }

    private suspend fun populateDatabaseIfNeeded() {
        try {
            val hanziDao = database.hanziCharDao()

            val existingHanziCount = withContext(Dispatchers.IO) {
                hanziDao.getHanziCharCount()
            }

            Log.d("XuexiApplication", "Número de caracteres Hanzi existentes: $existingHanziCount")

            if (existingHanziCount > 0) {
                Log.d("XuexiApplication", "Base de datos ya poblada con $existingHanziCount hanzi")
                return
            }

            Log.d("XuexiApplication", "Iniciando población de la base de datos...")

            val jsonString = loadJsonFromAssets("all_data538.json")
            if (jsonString == null) {
                Log.e("XuexiApplication", "No se pudo cargar el archivo JSON")
                return
            }

            val json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            }

            val chineseData = withContext(Dispatchers.Default) {
                try {
                    json.decodeFromString(ChineseData.serializer(), jsonString)
                } catch (e: Exception) {
                    Log.e("XuexiApplication", "Error al deserializar JSON: ${e.message}", e)
                    return@withContext ChineseData(emptyList(), emptyList(), emptyList())
                }
            }

            insertDataToDatabase(chineseData, json)

        } catch (e: Exception) {
            Log.e("XuexiApplication", "Error general en populateDatabaseIfNeeded: ${e.message}", e)
        }
    }

    private suspend fun insertDataToDatabase(chineseData: ChineseData, json: Json) {
        if (chineseData.hanzi.isEmpty() && chineseData.related_words.isEmpty() && chineseData.example_sentences.isEmpty()) {
            Log.w("XuexiApplication", "No hay datos para insertar")
            return
        }

        withContext(Dispatchers.IO) {
            try {
                // CAMBIO AQUÍ: Usar database.withTransaction en lugar de database.runInTransaction
                database.withTransaction { // Ahora este bloque es un contexto de corrutina
                    // 1. Insertar HanziChars
                    if (chineseData.hanzi.isNotEmpty()) {
                        val hanziEntities = chineseData.hanzi.map { hanziChar ->
                            HanziCharEntity(
                                character = hanziChar.character,
                                traditional = hanziChar.traditional,
                                pinyin = hanziChar.pinyin,
                                meaning = hanziChar.meaning,
                                difficulty = hanziChar.difficulty,
                                typeOfWord = hanziChar.typeOfWord,
                                frequency = hanziChar.frequency,
                                radicalsJson = hanziChar.radicalsJson?.let {
                                    json.encodeToString(JsonElement.serializer(), it)
                                },
                                strokeOrderVisualJson = hanziChar.strokeOrderVisualJson?.let {
                                    json.encodeToString(JsonElement.serializer(), it)
                                },
                                strokeOrderSVGJson = hanziChar.strokeOrderSVGJson?.let {
                                    json.encodeToString(JsonElement.serializer(), it)
                                },
                                strokeCount = hanziChar.strokeCount,
                                hskLevel = hanziChar.hskLevel,
                                isFavorite = hanziChar.isFavorite
                            )
                        }
                        database.hanziCharDao().insertAll(hanziEntities)
                        Log.d("XuexiApplication", "HanziChars insertados: ${hanziEntities.size}")
                    }

                    // 2. Insertar RelatedWords
                    if (chineseData.related_words.isNotEmpty()) {
                        val relatedWordEntities = chineseData.related_words.map { relatedWord ->
                            RelatedWordEntity(
                                simplified = relatedWord.simplified,
                                traditional = relatedWord.traditional,
                                pinyin = relatedWord.pinyin,
                                meaning = relatedWord.meaning,
                                typeOfWord = relatedWord.typeOfWord
                            )
                        }
                        database.relatedWordDao().insertAll(relatedWordEntities)
                        Log.d("XuexiApplication", "RelatedWords insertadas: ${relatedWordEntities.size}")
                    }

                    // 3. Insertar ExampleSentences
                    if (chineseData.example_sentences.isNotEmpty()) {
                        val exampleSentenceEntities = chineseData.example_sentences.map { exampleSentence ->
                            ExampleSentenceEntity(
                                chineseSimplified = exampleSentence.chineseSimplified,
                                chineseTraditional = exampleSentence.chineseTraditional,
                                pinyin = exampleSentence.pinyin,
                                translation = exampleSentence.translation
                            )
                        }
                        database.exampleSentenceDao().insertAll(exampleSentenceEntities)
                        Log.d("XuexiApplication", "ExampleSentences insertadas: ${exampleSentenceEntities.size}")
                    }
                } // Fin de database.withTransaction

                Log.d("XuexiApplication", "Población de la base de datos completada exitosamente")

            } catch (e: Exception) {
                Log.e("XuexiApplication", "Error al insertar datos en la base de datos: ${e.message}", e)
                throw e
            }
        }
    }

    private fun loadJsonFromAssets(fileName: String): String? {
        return try {
            assets.open(fileName).use { inputStream ->
                BufferedReader(InputStreamReader(inputStream, "UTF-8")).use { reader ->
                    reader.readText()
                }
            }
        } catch (e: IOException) {
            Log.e("XuexiApplication", "Error al leer archivo JSON '$fileName': ${e.message}", e)
            null
        }
    }
}