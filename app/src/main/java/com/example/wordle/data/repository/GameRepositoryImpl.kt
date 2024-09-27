package com.example.wordle.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.wordle.data.local.daos.GameRecordDao
import com.example.wordle.data.mapper.toGameRecord
import com.example.wordle.domain.model.GameRecord
import com.example.wordle.domain.repository.GameRecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
class GameRepositoryImpl @Inject constructor(
   private val gameRecordDao: GameRecordDao
): GameRecordRepository {

    override suspend fun getGameRecord(word: String): GameRecord {
        return gameRecordDao.getGameRecord(word).toGameRecord()
    }

    override fun getGameRecords(): Flow<List<GameRecord>> {
        return gameRecordDao.getGameRecords().map { it.map { it.toGameRecord() } }
    }

    override suspend fun insertGameRecord(gameRecord: GameRecord) {
        gameRecordDao.insertGameRecord(gameRecord.toGameRecord())
    }

    override suspend fun getGameRecordTotalScore(): Flow<Int> {
        return gameRecordDao.getTotalScore()
    }

    override suspend fun resetStats() {
        gameRecordDao.resetStats()
    }
}