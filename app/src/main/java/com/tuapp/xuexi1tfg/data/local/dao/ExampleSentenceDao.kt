package com.tuapp.xuexi1tfg.data.local.dao

// Archivo: app/src/main/java/com/tuapp/xuexi1tfg/data/db/ExampleSentenceDao.kt



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tuapp.xuexi1tfg.data.local.entities.ExampleSentenceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExampleSentenceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(sentences: List<ExampleSentenceEntity>)

    @Query("SELECT * FROM example_sentences WHERE chinese_simplified LIKE '%' || :query || '%' OR chinese_traditional LIKE '%' || :query || '%' OR translation LIKE '%' || :query || '%'")
    fun searchSentences(query: String): Flow<List<ExampleSentenceEntity>>

    @Query("SELECT * FROM example_sentences WHERE chinese_simplified LIKE '%' || :hanzi || '%'")
    fun getExampleSentencesForHanzi(hanzi: String): Flow<List<ExampleSentenceEntity>>

    @Query("SELECT * FROM example_sentences")
    fun getAllExampleSentences(): Flow<List<ExampleSentenceEntity>>
}