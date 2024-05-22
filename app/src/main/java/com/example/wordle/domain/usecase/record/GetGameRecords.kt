package com.example.wordle.domain.usecase.record

import com.example.wordle.domain.repository.GameRecordRepository
import javax.inject.Inject

class GetGameRecords @Inject constructor(private val repository: GameRecordRepository) {
    operator fun invoke() = repository.getGameRecords()
    suspend fun getGameTotalScore() = repository.getGameRecordTotalScore()
}