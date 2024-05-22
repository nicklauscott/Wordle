package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameRecord(
    @PrimaryKey val word: String,
    val date: Long,
    val durationInSeconds: Int,
    val attempts: String,
    val score: Int,
)

