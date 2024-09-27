package com.example.wordle.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wordle.data.local.entities.Word

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertWords(words: List<Word>)

    @Query("SELECT word FROM word WHERE used = 0 ORDER BY RANDOM() LIMIT 1")
    suspend fun getWord(): String

    @Query("UPDATE word SET used = 1 WHERE word = :word")
    suspend fun markWordAsUsed(word: String)

    @Query("SELECT COUNT(*) FROM word")
    suspend fun getUsedWordsCount(): Int

    @Query("SELECT count(*) > 0 FROM word WHERE word = :word")
    fun doesWordExist(word: String): Boolean

    @Query("UPDATE word SET used = 0")
    suspend fun resetWords()

}