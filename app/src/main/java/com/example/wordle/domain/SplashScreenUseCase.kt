package com.example.wordle.domain

import com.example.wordle.data.misc.words

class WordListUseCase {
    operator fun invoke() = words

    fun getWords(size: Int): List<String>{
        words.shuffle()
        return words.take(size)
    }
}