package com.tuapp.xuexi1tfg.data.repository

import com.tuapp.xuexi1tfg.data.local.dao.HanziCharDao
import com.tuapp.xuexi1tfg.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository para operaciones de caracteres y palabras
 *
 * Este repositorio actúa como intermediario entre el DAO y las capas superiores
 * de la aplicación (ViewModels, UseCases). Proporciona una API limpia y
 * puede combinar datos de múltiples fuentes si es necesario.
 */
@Singleton
class CharacterRepository @Inject constructor(
    private val characterDao: HanziCharDao
) {

    // =============================================
    // OPERACIONES DE CARACTERES
    // =============================================

    /**
     * Obtiene un carácter por su hanzi
     */
    suspend fun getCharacter(hanzi: String): Character? {
        return characterDao.getCharacter(hanzi)
    }

    /**
     * Obtiene caracteres por nivel HSK
     */
    suspend fun getCharactersByHskLevel(level: Int): List<Character> {
        return characterDao.getCharactersByHskLevel(level)
    }

    /**
     * Obtiene caracteres por rango de niveles HSK
     */
    suspend fun getCharactersByHskRange(minLevel: Int, maxLevel: Int): List<Character> {
        return characterDao.getCharactersByHskRange(minLevel, maxLevel)
    }

    /**
     * Busca caracteres por texto
     */
    suspend fun searchCharacters(query: String): List<Character> {
        return characterDao.searchCharacters(query)
    }

    /**
     * Obtiene caracteres por número de trazos
     */
    suspend fun getCharactersByStrokes(strokes: Int): List<Character> {
        return characterDao.getCharactersByStrokes(strokes)
    }

    /**
     * Obtiene caracteres por radical
     */
    suspend fun getCharactersByRadical(radical: String): List<Character> {
        return characterDao.getCharactersByRadical(radical)
    }

    /**
     * Obtiene los caracteres más frecuentes
     */
    suspend fun getMostFrequentCharacters(limit: Int = 100): List<Character> {
        return characterDao.getMostFrequentCharacters(limit)
    }

    /**
     * Obtiene los caracteres más productivos (que aparecen en más palabras)
     */
    suspend fun getMostProductiveCharacters(limit: Int = 50): List<Character> {
        return characterDao.getMostProductiveCharacters(limit)
    }

    // =============================================
    // OPERACIONES DE PALABRAS
    // =============================================

    /**
     * Obtiene una palabra por su forma simplificada
     */
    suspend fun getWord(simplified: String): Word? {
        return characterDao.getWord(simplified)
    }

    /**
     * Obtiene palabras por nivel HSK
     */
    suspend fun getWordsByHskLevel(level: Int): List<Word> {
        return characterDao.getWordsByHskLevel(level)
    }

    /**
     * Obtiene palabras por número de caracteres
     */
    suspend fun getWordsByCharacterCount(count: Int): List<Word> {
        return characterDao.getWordsByCharacterCount(count)
    }

    /**
     * Busca palabras por texto
     */
    suspend fun searchWords(query: String): List<Word> {
        return characterDao.searchWords(query)
    }

    /**
     * Obtiene las palabras más frecuentes
     */
    suspend fun getMostFrequentWords(limit: Int = 100): List<Word> {
        return characterDao.getMostFrequentWords(limit)
    }

    // =============================================
    // OPERACIONES CON RELACIONES
    // =============================================

    /**
     * Obtiene un carácter con todas las palabras que lo contienen
     */
    suspend fun getCharacterWithWords(hanzi: String): CharacterWithWords? {
        return characterDao.getCharacterWithWords(hanzi)
    }

    /**
     * Obtiene una palabra con todos sus caracteres
     */
    suspend fun getWordWithCharacters(simplified: String): WordWithCharacters? {
        return characterDao.getWordWithCharacters(simplified)
    }

    /**
     * Obtiene todas las palabras que contienen un carácter específico
     */
    suspend fun getWordsContainingCharacter(hanzi: String): List<Word> {
        return characterDao.getWordsContainingCharacter(hanzi)
    }

    /**
     * Obtiene palabras donde un carácter aparece en una posición específica
     */
    suspend fun getWordsWithCharacterAtPosition(hanzi: String, position: Int): List<Word> {
        return characterDao.getWordsWithCharacterAtPosition(hanzi, position)
    }

    // =============================================
    // OPERACIONES DE INSERCIÓN Y ACTUALIZACIÓN
    // =============================================

    /**
     * Inserta un carácter
     */
    suspend fun insertCharacter(character: Character) {
        characterDao.insertCharacter(character)
    }

    /**
     * Inserta múltiples caracteres
     */
    suspend fun insertCharacters(characters: List<Character>) {
        characterDao.insertCharacters(characters)
    }

    /**
     * Inserta una palabra
     */
    suspend fun insertWord(word: Word) {
        characterDao.insertWord(word)
    }

    /**
     * Inserta múltiples palabras
     */
    suspend fun insertWords(words: List<Word>) {
        characterDao.insertWords(words)
    }

    /**
     * Inserta una palabra con sus relaciones de caracteres
     * Esta función es útil para insertar una palabra completa de una vez
     */
    suspend fun insertWordWithCharacters(word: Word, characters: List<Character>) {
        // Primero insertar los caracteres (si no existen)
        characterDao.insertCharacters(characters)

        // Luego insertar la palabra
        characterDao.insertWord(word)

        // Finalmente crear las relaciones
        val wordCharacters = word.simplified.mapIndexed { index, char ->
            WordCharacter(
                wordId = word.simplified,
                charId = char.toString(),
                position = index
            )
        }
        characterDao.insertWordCharacters(wordCharacters)
    }

    /**
     * Actualiza las relaciones de una palabra
     * Útil cuando se modifica una palabra existente
     */
    suspend fun updateWordCharacterRelations(wordId: String) {
        // Eliminar relaciones existentes
        characterDao.deleteWordCharacterRelations(wordId)

        // Crear nuevas relaciones basadas en la palabra actual
        val word = characterDao.getWord(wordId)
        word?.let {
            val wordCharacters = it.simplified.mapIndexed { index, char ->
                WordCharacter(
                    wordId = it.simplified,
                    charId = char.toString(),
                    position = index
                )
            }
            characterDao.insertWordCharacters(wordCharacters)
        }
    }

    // =============================================
    // ESTADÍSTICAS Y MÉTRICAS
    // =============================================

    /**
     * Obtiene estadísticas generales de la base de datos
     */
    suspend fun getDatabaseStats(): DatabaseStats {
        val characterCount = characterDao.getCharacterCount()
        val wordCount = characterDao.getWordCount()

        return DatabaseStats(
            totalCharacters = characterCount,
            totalWords = wordCount
        )
    }

    // =============================================
    // FLOWS PARA UI REACTIVA
    // =============================================

    /**
     * Observa cambios en caracteres de un nivel HSK
     */
    fun observeCharactersByHskLevel(level: Int): Flow<List<Character>> {
        return characterDao.observeCharactersByHskLevel(level)
    }

    /**
     * Observa cambios en palabras de un nivel HSK
     */
    fun observeWordsByHskLevel(level: Int): Flow<List<Word>> {
        return characterDao.observeWordsByHskLevel(level)
    }
}

/**
 * Clase de datos para estadísticas de la base de datos
 */
data class DatabaseStats(
    val totalCharacters: Int,
    val totalWords: Int
) {
    val totalRelations: Int get() = totalWords // Aproximación
    val averageCharactersPerWord: Double get() = if (totalWords > 0) totalCharacters.toDouble() / totalWords else 0.0
}