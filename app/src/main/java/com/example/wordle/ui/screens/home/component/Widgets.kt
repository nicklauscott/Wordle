package com.example.wordle.ui.screens.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.wordle.R
import com.example.wordle.ui.screens.components.Cell
import com.example.wordle.ui.screens.components.CellStatus
import com.example.wordle.ui.screens.home.CharStatus
import com.example.wordle.ui.theme.getEffectColors


@Composable
fun RestartGameDialog(title: String, onRestart: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = title, style = MaterialTheme.typography.bodyLarge) },
        text = { Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Do you want to restart the game?",
                style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        }
        },
        icon = { Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart icon") },
        onDismissRequest = onDismiss,
        confirmButton = { IconButton(onClick = { onRestart() }) { Text(text = "Yes") } },
        shape = RoundedCornerShape(4.dp),
        containerColor = AlertDialogDefaults.containerColor.copy(alpha = 0.7f),
    )
}

@Composable
fun ShowCountDown(countDown: Long, screenHeight: Int) {
    val diff = (300000 - countDown) / 300000.0
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Card(modifier = Modifier
            .fillMaxWidth(diff.toFloat())
            .height(1.dp)
            .offset(y = screenHeight.dp / 8.2f),
            shape = RoundedCornerShape(1.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
        ) {}
    }
}

@Composable
fun ShowAnswer(modifier: Modifier = Modifier, answer: String) {
    Card(modifier = modifier
        .size(140.dp)
        .padding(4.dp),
        shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
        colors = CardDefaults.cardColors(containerColor = getEffectColors(false).notInWord.copy(alpha = 0.7f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 5.dp),
            verticalArrangement = Arrangement.Bottom, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = answer,
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun ShowRotateScreenMessage(modifier: Modifier = Modifier) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(R.drawable.rotate_icon),
            contentDescription = "back space icon",
            modifier = Modifier.size(150.dp),
            colorFilter = ColorFilter.tint(
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Oh no! We can't fit everything on your screen.",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Rotate your device to continue",
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

fun showGame(windowSizeClass: WindowSizeClass): Boolean {
    return with(windowSizeClass) {
        widthSizeClass == WindowWidthSizeClass.Compact
    }
}

@Composable
fun KeyBoardSection(modifier: Modifier = Modifier,
                    hardMode: Boolean = false,
                    contrast: Boolean,
                    charStatus: List<CharStatus>,
                    onSubmit: () -> Unit, onClear: () -> Unit,
                    onclick: (Char) -> Unit) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        listOf('q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 'p').forEach { char ->
            val used = charStatus.find { it.char == char }
            if (used == null) {
                Key(text = char, hardMode = hardMode) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status, contrast = contrast, hardMode = hardMode) { onclick(it) }
            }

        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        listOf('a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l').forEach { char ->
            val used = charStatus.find { it.char == char }
            if (used == null) {
                Key(text = char, hardMode = hardMode) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status, contrast = contrast, hardMode = hardMode) { onclick(it) }
            }
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        ActionKey(text = "Enter") { onSubmit() }

        listOf('z', 'x', 'c', 'v', 'b', 'n', 'm').forEach { char ->
            val used = charStatus.find { it.char == char }
            if (used == null) {
                Key(text = char, contrast = contrast, hardMode = hardMode) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status, contrast = contrast, hardMode = hardMode) { onclick(it) }
            }
        }

        ActionKey(resourceId = R.drawable.backspace_24) { onClear() }
    }
}


@Composable
fun HelpDialog(
    contrast: Boolean,
    onDismissRequest: () -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.background.copy(alpha = 1f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(vertical = 10.dp, horizontal = 8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(text = "How To Play", style = MaterialTheme.typography.bodyLarge,
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(5.dp))

            Text(text = "Guess the Wordle in 6 tries.", style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "•", style = MaterialTheme.typography.bodyLarge,
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

                Spacer(modifier = Modifier.width(3.dp))

                Text(text = " Each guess must be a valid 5-letter word.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            }

            Spacer(modifier = Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "•", style = MaterialTheme.typography.bodyLarge,
                    fontSize = 23.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

                Spacer(modifier = Modifier.width(3.dp))

                Text(text = " The color of the tiles will change to show how\n close your guess was to the word.",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Examples", style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 18.sp)

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                val weary = "weary"
                weary.forEach {
                    Cell(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = if (weary.indexOf(it) == 0) (-3).dp else (-3).dp),
                        text = it,
                        submitted = weary.indexOf(it) == 0,
                        contrast = contrast,
                        status = if (weary.indexOf(it) == 0) CellStatus.IN_RIGHT_SPOT else CellStatus.EMPTY,
                        charStatus = {})
                }
            }
            val weary = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                    append("W")
                }
                withStyle(style = MaterialTheme.typography.labelSmall.toSpanStyle()) {
                    append(" is in the word and in the correct spot.")
                }
            }
            Text(text = weary, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))


            Spacer(modifier = Modifier.height(10.dp))

            Row {
                val pills = "pills"
                pills.forEach {
                    Cell(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = if (pills.indexOf(it) == 1) (-3).dp else (-3).dp),
                        text = it,
                        submitted = pills.indexOf(it) == 1,
                        contrast = contrast,
                        status = if (pills.indexOf(it) == 1) CellStatus.IN_WORD else CellStatus.EMPTY,
                        charStatus = {})
                }
            }
            val pills = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                    append("I")
                }
                withStyle(style = MaterialTheme.typography.labelSmall.toSpanStyle()) {
                    append(" is in the word but in the wrong spot.")
                }
            }
            Text(text = pills, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))


            Spacer(modifier = Modifier.height(10.dp))

            Row {
                val vague = "vague"
                vague.forEach {
                    Cell(
                        modifier = Modifier
                            .size(40.dp)
                            .offset(x = if (vague.indexOf(it) == 3) (-3).dp else (-3).dp),
                        text = it,
                        submitted = vague.indexOf(it) == 3,
                        contrast = contrast,
                        status = if (vague.indexOf(it) == 3) CellStatus.NOT_IN_WORD else CellStatus.EMPTY,
                        charStatus = {})
                }
            }
            val vague = buildAnnotatedString {
                withStyle(style = MaterialTheme.typography.bodySmall.toSpanStyle()) {
                    append("U")
                }
                withStyle(style = MaterialTheme.typography.labelSmall.toSpanStyle()) {
                    append(" is not in the word in any spot.")
                }
            }
            Text(text = vague, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f))

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}