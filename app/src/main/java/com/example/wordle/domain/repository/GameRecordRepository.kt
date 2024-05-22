package com.example.wordle.domain.repository

import com.example.wordle.domain.model.GameRecord
import kotlinx.coroutines.flow.Flow

interface GameRecordRepository {
    suspend fun getGameRecord(word: String): GameRecord
    fun getGameRecords(): Flow<List<GameRecord>>
    suspend fun insertGameRecord(gameRecord: GameRecord)
    suspend fun getGameRecordTotalScore(): Flow<Int>
}