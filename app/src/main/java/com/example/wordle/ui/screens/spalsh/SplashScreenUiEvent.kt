package com.example.wordle.ui.screens.spalsh

sealed class SplashScreenUiEvent {
    data class Submit(val guessFeedBack: List<GuessFeedBack>?): SplashScreenUiEvent()
}

enum class CharStatus {
    IN_RIGHT_SPOT, IN_WORD, NOT_IN_WORD
}

data class GuessFeedBack(val char: Char, val status: CharStatus, val position: Int)