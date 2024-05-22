package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings(
    @PrimaryKey val settingsId: Int = 1,
    val username: String = "Guest",
    val contrast: Boolean = false
)
