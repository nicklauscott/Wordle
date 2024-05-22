package com.example.wordle.ui.screens.home

import com.example.wordle.ui.screens.components.CellStatus


data class HomeScreenUIState(
    val guesses: UserGuesses = UserGuesses(),
    val word: String = "",
    val attempts: Int = 0
)

data class Guess(
    val userGuess: String = "     ",
    val submitted: Boolean = false
)

data class UserGuesses(
    val first: Guess = Guess(),
    val second: Guess = Guess(),
    val third: Guess = Guess(),
    val fourth: Guess = Guess(),
    val fifth: Guess = Guess(),
    val sixth: Guess = Guess(),
)

data class CharStatus(
    val char: Char,
    val status: CellStatus,
    val clickable: Boolean
)