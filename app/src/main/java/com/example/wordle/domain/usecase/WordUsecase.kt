package com.example.wordle.domain.usecase

import com.example.wordle.domain.usecase.word.GetWord
import javax.inject.Inject

class WordUsecase @Inject constructor(val getWord: GetWord)