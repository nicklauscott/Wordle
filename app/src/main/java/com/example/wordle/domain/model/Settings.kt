package com.example.wordle.domain.model

data class Settings(
    val settingsId: Int = 1,
    val username: String = "Guest",
    val hardMode: Boolean = false,
    val contrast: Boolean = false
)
