package com.example.wordle.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wordle.data.local.entities.GameRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertGameRecord(gameRecord: GameRecord)

    @Query("SELECT * FROM gamerecord")
    fun getGameRecords(): Flow<List<GameRecord>>

    @Query("SELECT * FROM gamerecord WHERE word = :word ")
    fun getGameRecord(word: String): GameRecord

    @Query("SELECT sum(score) FROM gamerecord")
    fun getTotalScore(): Flow<Int>

}