package com.example.wordle.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.wordle.domain.model.GameRecord
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

@RequiresApi(Build.VERSION_CODES.O)
fun GameRecord.toGameRecord(): com.example.wordle.data.local.entities.GameRecord {
    val gameId = (0..9999).random()
    val date = date.localDateTimeToLong()
    val durationInSeconds = durationInSeconds
    val word = word
    val attempts = attempts.attemptsToString()
    val score = score
    return com.example.wordle.data.local.entities.GameRecord(
        word, date, durationInSeconds, attempts, score
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun com.example.wordle.data.local.entities.GameRecord.toGameRecord(): GameRecord {
    val date = date.longToLocalDateTime()
    val durationInSeconds = durationInSeconds
    val word = word
    val attempts = attempts.attemptsToList()
    val score = score
    return GameRecord(word, date, durationInSeconds, attempts, score)
}


fun String.attemptsToList(): List<String> {
    return this.split(",")
}

fun List<String>.attemptsToString(): String {
    return this.joinToString(",")
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.localDateTimeToLong(): Long {
    return this.toInstant(ZoneOffset.UTC).toEpochMilli()
}

@RequiresApi(Build.VERSION_CODES.O)
fun Long.longToLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), ZoneOffset.UTC)
}