package com.example.wordle.ui.screens.history.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.domain.model.GameRecord
import com.example.wordle.ui.formatDateAndTime
import com.example.wordle.ui.screens.components.CellRow
import com.example.wordle.ui.secondsToMinutes

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun GameRecordCell(modifier: Modifier = Modifier, contrast: Boolean = false, gameNo: Int, gameRecord: GameRecord) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 8.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 1f)
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column {
                gameRecord.attempts.forEach { guess ->
                    CellRow(modifier = Modifier,
                        submitted = guess.isNotBlank(), userGuess = guess.ifBlank { "     " },
                        contrast = !contrast,
                        word = gameRecord.word) {  }
                }
            }
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))


            Column {
                Spacer(Modifier.height(8.dp))
                val (date, time) = formatDateAndTime(gameRecord.date)
                RecordCell(label = "Game No", value = gameNo.toString())
                RecordCell(label = "Word", value = gameRecord.word.uppercase())
                RecordCell(label = "Attempts", value = gameRecord.attempts.filter { it.isNotBlank() }.size.toString())
                RecordCell(label = "Score", value = gameRecord.score.toString())
                RecordCell(label = "Game Length", value = secondsToMinutes(gameRecord.durationInSeconds))
                RecordCell(label = "Date", value = date)
                RecordCell(label = "Time", value = time)

            }

        }
    }
}

@Composable
fun RecordCell(modifier: Modifier = Modifier, symbol: String = ":", label: String, value: String) {

    Row(
        modifier = modifier.width(380.dp)
            .padding(vertical = 4.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(text = " $label $symbol",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))



        Text(text = " $value",
            style = MaterialTheme.typography.labelSmall,
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f))
    }
}