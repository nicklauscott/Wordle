package com.example.wordle.ui.screens.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.wordle.ui.screens.home.CharStatus
import com.example.wordle.ui.theme.getEffectColors
import com.example.wordle.ui.triggerVibration

enum class CellStatus {
    EMPTY, IN_RIGHT_SPOT, IN_WORD, NOT_IN_WORD
}



@Preview
@Composable
fun CellRowPreview(modifier: Modifier = Modifier, word: String = "alert") {
    Column(modifier = modifier) {
        CellRow(userGuess = "     ", word = word)
        CellRow(userGuess = "     ", word = word)
        CellRow(userGuess = "     ", word = word)
        CellRow(userGuess = "     ", word = word)
        CellRow(userGuess = "     ", word = word)
        CellRow(userGuess = "     ", word = word)
    }
}


@Composable
fun CellRow(modifier: Modifier = Modifier, submitted: Boolean = false,
            vibrate: Boolean = true,
            userGuess: String = "     ", word: String, charStatus: (List<CharStatus>) -> Unit = {}) {

    val context = LocalContext.current
    val charStatusList = mutableListOf<CharStatus>()

    val offsetX = remember {
        Animatable(0f)
    }

    LaunchedEffect(submitted) {
        if (submitted && userGuess != word) {
            if (vibrate) triggerVibration(context)
            repeat(1) { // Change the repeat count as needed
                // Animate to the right
                offsetX.animateTo(
                    targetValue = 30f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInBounce
                    )
                )
                // Animate back to the left
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInBounce
                    )
                )

                // Animate to the left
                offsetX.animateTo(
                    targetValue = -30f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInBounce
                    )
                )
                // Animate back to the right
                offsetX.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = 50,
                        easing = EaseInBounce
                    )
                )
            }
        }
        charStatus(charStatusList)
    }

    Row(
        modifier = Modifier.offset(x = offsetX.value.dp)
    ) {
        userGuess.take(5).forEach {
            val isInRightPosition = it == word[userGuess.indexOf(it)]
            val isInWord = it in word
            val isNotInWord = it !in word

            val status = if (isInRightPosition) CellStatus.IN_RIGHT_SPOT
            else if (isInWord) CellStatus.IN_WORD
            else if (isNotInWord) CellStatus.NOT_IN_WORD else CellStatus.EMPTY

            Cell(modifier = modifier, text = it, submitted = submitted, status = status, contrast = false) { charStatus ->
                charStatusList.add(charStatus)
            }
        }
    }
}


@Composable
fun Cell(
    modifier: Modifier = Modifier, text: Char = ' ', submitted: Boolean = false,
    status: CellStatus? = null, contrast: Boolean = false, charStatus: (CharStatus) -> Unit
) {

    if (submitted) {
        charStatus(CharStatus(text, status ?: CellStatus.EMPTY, false))
    }

    Card(
        modifier = modifier
            .padding(3.dp)
            .size(70.dp),
        shape = RectangleShape,
        border = BorderStroke(
            width = if (submitted) 0.dp else 1.dp,
            color = if (submitted) Color.Transparent
                else {
                    if (text == ' ') MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.secondary
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (submitted) {
                when (status) {
                    CellStatus.EMPTY -> Color.Transparent
                    CellStatus.IN_RIGHT_SPOT -> getEffectColors(contrast).isInRightPosition
                    CellStatus.IN_WORD -> getEffectColors(contrast).isInWord
                    CellStatus.NOT_IN_WORD -> getEffectColors(contrast).notInWord
                    null -> Color.Transparent
                }
            }else Color.Transparent
        )
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = text.toString().uppercase(),
                color = if (submitted) Color.White
                else MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}


fun elevateCellRow(
    elevate: Boolean, elevation: Float,
    rowScale: Float, isElevated: Boolean, win: Boolean): Modifier {
    return if (elevate) Modifier
        .graphicsLayer(
            shadowElevation = elevation,
            scaleX = rowScale,
            scaleY = rowScale)
        .zIndex(if (isElevated) 1f else 0f)
    else if (win) Modifier.graphicsLayer(scaleX = 0.6f, scaleY = 0.6f)
    else Modifier
}