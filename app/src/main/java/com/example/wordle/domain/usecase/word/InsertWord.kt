package com.example.wordle.domain.usecase.word

import com.example.wordle.data.local.entities.Word
import com.example.wordle.data.misc.words
import com.example.wordle.domain.repository.WordRepository
import javax.inject.Inject

class InsertWord @Inject constructor(private val repository: WordRepository) {
    suspend operator fun invoke() {
        repository.insertWords(words.map { Word(it) })
    }
}