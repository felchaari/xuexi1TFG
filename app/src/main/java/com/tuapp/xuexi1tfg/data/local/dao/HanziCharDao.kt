package com.tuapp.xuexi1tfg.data.local.dao

// Archivo: app/src/main/java/com/tuapp/xuexi1tfg/data/db/HanziCharDao.kt



import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tuapp.xuexi1tfg.data.local.entities.HanziCharEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HanziCharDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(hanziChars: List<HanziCharEntity>)

    @Update
    suspend fun updateHanziChar(hanziChar: HanziCharEntity)

    @Query("SELECT * FROM hanzi_chars WHERE character = :hanziChar")
    fun getHanziChar(hanziChar: String): Flow<HanziCharEntity?>

    @Query("SELECT * FROM hanzi_chars WHERE hsk_level = :hskLevel")
    fun getHanziByHskLevel(hskLevel: Int): Flow<List<HanziCharEntity>>

    @Query("SELECT * FROM hanzi_chars ORDER BY RANDOM() LIMIT 1")
    fun getRandomHanzi(): Flow<HanziCharEntity?>

    @Query("SELECT * FROM hanzi_chars WHERE is_favorite = 1")
    fun getFavoriteHanzi(): Flow<List<HanziCharEntity>>

    @Query("SELECT * FROM hanzi_chars WHERE character LIKE '%' || :query || '%' OR pinyin LIKE '%' || :query || '%' OR meaning LIKE '%' || :query || '%'")
    fun searchHanzi(query: String): Flow<List<HanziCharEntity>>

    @Query("SELECT * FROM hanzi_chars WHERE next_review <= :currentTime ORDER BY next_review ASC")
    fun getHanziForReview(currentTime: Long): Flow<List<HanziCharEntity>>

    @Query("SELECT COUNT(character) FROM hanzi_chars")
    suspend fun getHanziCharCount(): Int

}