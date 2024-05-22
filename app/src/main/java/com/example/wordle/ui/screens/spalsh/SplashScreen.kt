package com.example.wordle.ui.screens.spalsh

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.navigation.NavController
import com.example.wordle.ui.navigation.Screens
import com.example.wordle.ui.screens.components.CellRow
import com.example.wordle.ui.screens.components.elevateCellRow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(navController: NavController, viewModel: SplashScreenViewModel) {

    var settingsAndWordsExist by remember { mutableStateOf(false) }
    val scale = remember { Animatable(0f) }
    var win by remember { mutableStateOf(false) }
    var isElevated by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(targetValue = if (isElevated) 20f else 0f, label = "elevation")
    val rowScale by animateFloatAsState(targetValue = if (isElevated) 1.5f else 1f, label = "rowScale")

    LaunchedEffect(true) {
        viewModel.onEvent(SplashScreenUiEvent.Submit(guessFeedBack = null))

        scale.animateTo(0.7f, animationSpec = tween(
            durationMillis = 2000,
            easing = EaseInBounce
        ))

        viewModel.events.collectLatest {
            when(it) {
                is SplashScreenViewModel.SplashScreenUiChannel.Guess -> {
                    viewModel.onEvent(SplashScreenUiEvent.Submit(guessFeedBack = null))
                }

                is SplashScreenViewModel.SplashScreenUiChannel.GuessFeedback -> {
                    delay(500)
                    viewModel.onEvent(SplashScreenUiEvent.Submit(guessFeedBack = it.feedback))
                }

                SplashScreenViewModel.SplashScreenUiChannel.WIn -> {
                    isElevated = true
                    win = true

                    if (settingsAndWordsExist) {
                        delay(1000)
                        scale.animateTo(0f, animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing))
                        navController.popBackStack()
                        navController.navigate(Screens.Home.route)
                    }
                }

                SplashScreenViewModel.SplashScreenUiChannel.GameOver -> {
                    if (settingsAndWordsExist) {
                        launch {
                            delay(1100)
                            navController.popBackStack()
                            navController.navigate(Screens.Home.route)
                        }
                    }
                    scale.animateTo(0f, animationSpec = tween(durationMillis = 1000, easing = FastOutLinearInEasing))
                }

                is SplashScreenViewModel.SplashScreenUiChannel.SettingsAndWordExist -> {
                    settingsAndWordsExist = it.isNotEmpty
                }
            }
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)
        .scale(scale.value),
        contentAlignment = Alignment.Center) {

        Column {
            repeat(6) {
                CellRow(
                    modifier = elevateCellRow(viewModel.attempts.value == it + 1, elevation, rowScale, isElevated, win),
                    vibrate = false,
                    submitted = true,
                    contrast = viewModel.settings.value?.contrast ?: false,
                    userGuess = viewModel.guessedWords.value[it],
                    word = viewModel.word)
            }
        }
    }
}


