package com.tuapp.xuexi1tfg.data.local.dao

// Archivo: app/src/main/java/com/tuapp/xuexi1tfg/data/db/RelatedWordDao.kt


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.xuexi1tfg.data.local.entities.RelatedWordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RelatedWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<RelatedWordEntity>)

    @Query("SELECT * FROM related_words WHERE simplified = :simplifiedWord OR traditional = :simplifiedWord")
    fun getRelatedWordsForHanzi(simplifiedWord: String): Flow<List<RelatedWordEntity>>

    @Query("SELECT * FROM related_words WHERE simplified LIKE '%' || :query || '%' OR traditional LIKE '%' || :query || '%' OR pinyin LIKE '%' || :query || '%' OR meaning LIKE '%' || :query || '%' OR type_of_word LIKE '½' || :query || '%'")
    fun searchWords(query: String): Flow<List<RelatedWordEntity>>

    @Query("SELECT * FROM related_words")
    fun getAllRelatedWords(): Flow<List<RelatedWordEntity>>
}