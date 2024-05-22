package com.example.wordle.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wordle.R
import com.example.wordle.ui.navigation.Screens
import com.example.wordle.ui.screens.components.AppTopBar
import com.example.wordle.ui.screens.home.component.ActionKey
import com.example.wordle.ui.screens.components.CellRow
import com.example.wordle.ui.screens.components.CellStatus
import com.example.wordle.ui.screens.home.component.HomeScreenUiChannel
import com.example.wordle.ui.screens.home.component.HomeScreenUiEvent
import com.example.wordle.ui.screens.home.component.Key
import com.example.wordle.ui.screens.home.component.KeyStatus
import com.example.wordle.ui.theme.getEffectColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, windowSizeClass: WindowSizeClass,
    contrast: Boolean = false,
    navController: NavController = rememberNavController()) {

    val height = LocalConfiguration.current.screenHeightDp

    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val viewModel: HomeScreenViewModel = hiltViewModel()
    val state  = viewModel.state
    val countDown  = rememberSaveable { mutableIntStateOf(300) }

    var showAnswer by rememberSaveable { mutableStateOf(false) }
    var showResetDialog by rememberSaveable { mutableStateOf(false) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(viewModel.playing) {
        while (viewModel.playing.value == true) {
            if (countDown.intValue % 2 == 0) {
                viewModel.onEvent(HomeScreenUiEvent.CountDown(countDown.intValue))
            }

            if (countDown.intValue == 0) {
                viewModel.onEvent(HomeScreenUiEvent.Submit)
            }
            delay(1000)
            countDown.intValue -= 1
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeScreenUiChannel.GameOver -> { showAnswer = true }
                is HomeScreenUiChannel.NotInWordList -> {
                    scope.launch {
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar("")
                    }
                }
                HomeScreenUiChannel.Win -> {
                    dialogMessage = "You won!"
                    showResetDialog = true
                }
            }
        }
    }


    if (showResetDialog) {
        RestartGameDialog(
            title = dialogMessage,
            onRestart = {
                showResetDialog = false
                countDown.intValue = 300
                viewModel.onEvent(HomeScreenUiEvent.Restart)
        }, onDismiss = { showResetDialog = false })
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Wordle", content = {
                IconButton(onClick = { navController.navigate(Screens.History.route) }) {
                    Icon(imageVector = Icons.Default.List, contentDescription = "History icon")
                }
                IconButton(onClick = { showAnswer = !showAnswer }) {
                    Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings icon")
                }
            })
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Card(modifier = modifier
                    .height(55.dp)
                    .padding(4.dp)
                    .offset(y = (-height / 1.17).dp),
                    shape = RoundedCornerShape(bottomStart = 4.dp, bottomEnd = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = getEffectColors(false).notInWord)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 5.dp),
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = "Not in word list",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                }
            }
        }
    ) { paddingValues ->

        if (!showGame(windowSizeClass)) {
            ShowRotateScreenMessage()
            return@Scaffold
        }

        when {
            viewModel.playing.value == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
                }
            }
            else -> {
                Box(contentAlignment = Alignment.TopCenter) {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(start = 5.dp, end = 5.dp, bottom = 16.dp),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        CellRow(submitted = state.value.guesses.first.submitted, userGuess = state.value.guesses.first.userGuess, word = state.value.word) {
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(submitted = state.value.guesses.second.submitted, userGuess = state.value.guesses.second.userGuess, word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(submitted = state.value.guesses.third.submitted, userGuess = state.value.guesses.third.userGuess, word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(submitted = state.value.guesses.fourth.submitted, userGuess = state.value.guesses.fourth.userGuess, word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(submitted = state.value.guesses.fifth.submitted, userGuess = state.value.guesses.fifth.userGuess, word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(submitted = state.value.guesses.sixth.submitted, userGuess = state.value.guesses.sixth.userGuess, word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        KeyBoardSection(
                            charStatus = viewModel.charStatus.value,
                            onSubmit = { viewModel.onEvent(HomeScreenUiEvent.Submit) },
                            onClear = { viewModel.onEvent(HomeScreenUiEvent.Clear) }) { char ->
                            viewModel.onEvent(HomeScreenUiEvent.CharSelect(char))
                        }
                    }

                    ShowCountDown(countDown = countDown.intValue, screenHeight = height)

                    AnimatedVisibility(visible = showAnswer,
                        enter = slideInVertically(animationSpec = tween(durationMillis = 300, easing = EaseInBounce)) +
                                fadeIn(animationSpec = tween(durationMillis = 300, easing = LinearEasing))
                    ) {
                        ShowAnswer(answer = state.value.word.uppercase())
                    }
                }
            }
        }

    }
}

@Composable
fun RestartGameDialog(title: String, onRestart: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        title = { Text(text = title, style = MaterialTheme.typography.bodyLarge) },
        text = { Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "Do you want to restart the game?",
                style = MaterialTheme.typography.bodySmall)
            }},
        icon = { Icon(imageVector = Icons.Default.Refresh, contentDescription = "Restart icon") },
        onDismissRequest = onDismiss,
        confirmButton = { IconButton(onClick = { onRestart() }) { Text(text = "Restart") } },
        shape = RoundedCornerShape(4.dp)
    )
}

@Composable
fun ShowCountDown(countDown: Int, screenHeight: Int) {
    val diff = countDown.toDouble() / 300
    Card(modifier = Modifier
        .fillMaxWidth(diff.toFloat())
        .height(1.dp)
        .offset(y = screenHeight.dp / 8.2f),
        shape = RoundedCornerShape(1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f))
    ) {}
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

private fun showGame(windowSizeClass: WindowSizeClass): Boolean {
    return with(windowSizeClass) {
        widthSizeClass == WindowWidthSizeClass.Compact
    }
}

@Composable
fun KeyBoardSection(modifier: Modifier = Modifier,
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
                Key(text = char) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status) { onclick(it) }
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
                Key(text = char) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status) { onclick(it) }
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
                Key(text = char) { onclick(it) }
            }else {
                val status = when (used.status) {
                    CellStatus.EMPTY -> null
                    CellStatus.IN_RIGHT_SPOT -> KeyStatus.IN_RIGHT_SPOT
                    CellStatus.IN_WORD -> KeyStatus.IN_WORD
                    CellStatus.NOT_IN_WORD -> KeyStatus.NOT_IN_WORD
                }
                Key(text = char, status = status) { onclick(it) }
            }
        }

        ActionKey(resourceId = R.drawable.backspace_24) { onClear() }
    }
}

