package com.example.wordle.domain.usecase.record

import com.example.wordle.domain.repository.GameRecordRepository
import javax.inject.Inject

class GetGameRecord @Inject constructor(private val repository: GameRecordRepository) {
    suspend operator fun invoke(id: Int) = repository.getGameRecord(id)
}