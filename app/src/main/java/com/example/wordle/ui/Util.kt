package com.example.wordle.ui

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import com.example.wordle.domain.model.GameRecord
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun triggerVibration(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
    }else {
        vibrator.vibrate(100)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDateAndTime(localDateTime: LocalDateTime): Pair<String, String>{
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
    return Pair(localDateTime.format(dateFormatter), localDateTime.format(timeFormatter))
}

fun printToConsole(vararg message: Any) {
    print("------------------- ")
    message.forEach { print(" $it ->") }
    println()
}

fun getWinningStreak(gameRecords: List<GameRecord>): Int {
    var winCount = 0
    gameRecords.sortedByDescending { it.date }.map { it.score }.forEach {
        if (it != 0) {
            winCount++
        } else {
            return winCount
        }
    }
    return winCount
}

fun getMaxWinningStreak(gameRecords: List<GameRecord>): Int {
    var winCount = 0
    var maxWinCount = 0
    gameRecords.sortedByDescending { it.date }.map { it.score }.forEach {
        if (it != 0) {
            winCount++
        } else {
            if (winCount > maxWinCount) {
                maxWinCount = winCount
            }
            winCount = 0
        }
    }
    return maxWinCount
}

fun Int.getRowByScore(): Int {
    return when(this) {
        1 -> 6 2 -> 5 3 -> 4 4 -> 3 5 -> 2 6 -> 1 else -> 0
    }
}

fun secondsToMinutes(seconds: Int): String {
    val minutes = seconds / 60
    val remainingSeconds = seconds % 60
    return String.format("%2dm %02ds", minutes, remainingSeconds)
}