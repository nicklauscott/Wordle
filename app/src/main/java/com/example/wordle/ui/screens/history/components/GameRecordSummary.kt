package com.example.wordle.ui.screens.history.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.domain.model.GameRecord
import com.example.wordle.ui.getMaxWinningStreak
import com.example.wordle.ui.getRowByScore
import com.example.wordle.ui.getWinningStreak
import com.example.wordle.ui.theme.getEffectColors

@Composable
fun GameRecordSummary(
    contrast: Boolean = false, gameRecords: List<GameRecord> = emptyList(),
    resetStats: () -> Unit = {}
) {

    val winSize = gameRecords.filter { it.score != 0 }.size
    val winRate = ((winSize / gameRecords.size.toDouble()) * 100).toInt()
    val bestTries = gameRecords.filter { it.score != 0 }.groupBy { it.score }
        .map { mapOf(it.key.getRowByScore() to it.value.size) }
        .sortedByDescending { it.values.first() }


    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp).padding(top = 16.dp)
                .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "Statistics", style = MaterialTheme.typography.bodyLarge,
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.onBackground)
        }

        Spacer(modifier = Modifier.height(16.dp))



        Row {
            StatCell(label = "Games Played", value = gameRecords.size.toString())
            StatCell(label = "Games Won", value = winSize.toString())
            StatCell(label = "% of Win", value = winRate.toString())
        }

        Row {

            val bestTry = bestTries.firstOrNull().let { it?.keys?.first() } ?: 0

            StatCell(label = "Best Try", value = "#$bestTry")
            StatCell(label = "Current Streak", value = getWinningStreak(gameRecords).toString())
            StatCell(label = "Max Streak", value = getMaxWinningStreak(gameRecords).toString())
        }


        Spacer(modifier = Modifier.height(16.dp))

        WinningCells(gameRecords = gameRecords, contrast = contrast)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = resetStats) {
            Text(text = "Reset Stats", style = MaterialTheme.typography.labelMedium)
        }
    }
}


@Composable
fun StatCell(modifier: Modifier = Modifier, label: String, value: String) {
    Card(
        modifier = Modifier.padding(4.dp),
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.25f)
        )
    ) {
        Column(
            modifier = modifier
                .size(120.dp, 80.dp)
                .padding(4.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = value, style = MaterialTheme.typography.bodyLarge,
                fontSize = 28.sp, fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground)

            Text(text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f))
        }
    }
}


@Composable
fun WinningCells(gameRecords: List<GameRecord>, contrast: Boolean) {

    Text(text = "Best tries distribution", style = MaterialTheme.typography.bodyLarge,
        fontSize = 23.sp,
        color = MaterialTheme.colorScheme.onBackground)

    Spacer(modifier = Modifier.height(8.dp))

    val rowAndCount = gameRecords.groupBy { it.score }
        .map { mapOf(it.key.getRowByScore() to it.value.size) }
        .sortedByDescending { it.keys.first() }

    (1..6).forEach { cellNo ->

        val currentRow = rowAndCount.find { it.keys.first() == cellNo }

        currentRow?.let {
            val percentage = (it.values.first() * 100) / gameRecords.size
            DistributionCell(cellNo = cellNo, percentage = percentage,
                size = it.values.first(), contrast = contrast)
            return@forEach
        }

        DistributionCell(cellNo = cellNo, size = 0, contrast = contrast)
    }
}


@Composable
fun DistributionCell(cellNo: Int, percentage: Int = 0, size: Int, contrast: Boolean) {
    Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically) {

        Text(
            modifier = Modifier.weight(1f),
            text = "#$cellNo", style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.85f))

        Surface(
            Modifier
                .weight(9f)
                .height(30.dp),
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)),
            shape = RoundedCornerShape(12.dp),

            ) {


            Box {
                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(percentage.toFloat() / 100f)
                        .clip(RoundedCornerShape(12.dp)),
                    thickness = 50.dp,
                    color = getEffectColors(contrast).isInRightPosition.copy(alpha = 0.85f)
                )
                Text(text = "$percentage%", style = MaterialTheme.typography.bodyLarge,
                    fontSize = 13.sp,
                    modifier = Modifier.align(Alignment.CenterStart)
                        .offset(x  = (percentage * 2.8).dp).padding(horizontal = 4.dp),
                    color = MaterialTheme.colorScheme.onBackground)
            }

        }

        Text(
            modifier = Modifier.weight(1f),
            text = size.toString(), style = MaterialTheme.typography.bodyLarge,
            fontSize = 15.sp, textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground)
    }
}