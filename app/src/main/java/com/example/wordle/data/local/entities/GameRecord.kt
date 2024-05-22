package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GameRecord(
    @PrimaryKey(autoGenerate = true) val gameId: Int,
    val date: Long,
    val durationInSeconds: Int,
    val word: String,
    val attempts: String,
    val score: Int,
)

