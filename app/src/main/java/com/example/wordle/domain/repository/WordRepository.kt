package com.example.wordle.domain.repository

import com.example.wordle.data.local.entities.Word

interface WordRepository {
    suspend fun getWord(): String
    suspend fun countWords(): Int
    suspend fun insertWords(words: List<Word>)
    suspend fun doesWordExist(word: String): Boolean
}