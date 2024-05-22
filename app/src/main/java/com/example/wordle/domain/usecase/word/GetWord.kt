package com.example.wordle.domain.usecase.word

import com.example.wordle.data.local.entities.Word
import com.example.wordle.data.misc.words
import com.example.wordle.domain.repository.WordRepository
import javax.inject.Inject

class GetWord @Inject constructor(private val repository: WordRepository) {
    suspend operator fun invoke() = repository.getWord()

    suspend fun count() = repository.countWords()

    suspend fun doesWordExist(word: String) = repository.doesWordExist(word)

}