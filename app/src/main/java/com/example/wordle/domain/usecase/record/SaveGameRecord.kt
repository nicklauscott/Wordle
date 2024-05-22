package com.example.wordle.domain.usecase.record

import com.example.wordle.domain.model.GameRecord
import com.example.wordle.domain.repository.GameRecordRepository
import javax.inject.Inject

class SaveGameRecord @Inject constructor(private val repository: GameRecordRepository) {
    suspend operator fun invoke(record: GameRecord) = repository.insertGameRecord(record)
}