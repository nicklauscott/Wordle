package com.example.wordle.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class GameRecord(
    val word: String,
    val date: LocalDateTime = LocalDateTime.now(),
    val durationInSeconds: Int,
    val attempts: List<String>,
    val score: Int,
)

