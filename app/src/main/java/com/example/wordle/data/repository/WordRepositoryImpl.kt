package com.example.wordle.data.repository

import com.example.wordle.data.local.daos.WordDao
import com.example.wordle.data.local.entities.Word
import com.example.wordle.domain.repository.WordRepository
import javax.inject.Inject

class WordRepositoryImpl @Inject constructor(private val wordDao: WordDao): WordRepository {
    override suspend fun getWord(): String {
        val word = wordDao.getWord()
        wordDao.markWordAsUsed(word)
        return word
    }

    override suspend fun countWords(): Int {
        return wordDao.getUsedWordsCount()
    }

    override suspend fun insertWords(words: List<Word>) {
        wordDao.insertWords(words)
    }

    override suspend fun resetWords() {
        wordDao.resetWords()
    }

    override suspend fun doesWordExist(word: String): Boolean {
        return wordDao.doesWordExist(word)
    }
}