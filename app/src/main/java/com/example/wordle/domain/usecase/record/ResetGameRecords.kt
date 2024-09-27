package com.example.wordle.domain.usecase.record

import com.example.wordle.domain.repository.GameRecordRepository
import com.example.wordle.domain.repository.WordRepository
import javax.inject.Inject

class ResetGameRecords @Inject constructor(
    private val repository: GameRecordRepository,
    private val wordRepository: WordRepository
    ) {
    suspend operator fun invoke()  {
        wordRepository.resetWords()
        repository.resetStats()
    }
}