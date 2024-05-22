package com.example.wordle.domain.usecase

import com.example.wordle.domain.usecase.word.GetWord
import com.example.wordle.domain.usecase.word.InsertWord
import javax.inject.Inject

class SplashScreeUsecase @Inject constructor(val getWord: GetWord, val insertWord: InsertWord)