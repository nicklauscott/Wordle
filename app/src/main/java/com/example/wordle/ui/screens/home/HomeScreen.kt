package com.example.wordle.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInBounce
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.wordle.R
import com.example.wordle.ui.navigation.Screens
import com.example.wordle.ui.screens.components.AppTopBar
import com.example.wordle.ui.screens.components.Cell
import com.example.wordle.ui.screens.home.component.ActionKey
import com.example.wordle.ui.screens.components.CellRow
import com.example.wordle.ui.screens.components.CellStatus
import com.example.wordle.ui.screens.components.elevateCellRow
import com.example.wordle.ui.screens.home.component.HelpDialog
import com.example.wordle.ui.screens.home.component.Key
import com.example.wordle.ui.screens.home.component.KeyBoardSection
import com.example.wordle.ui.screens.home.component.KeyStatus
import com.example.wordle.ui.screens.home.component.RestartGameDialog
import com.example.wordle.ui.screens.home.component.ShowAnswer
import com.example.wordle.ui.screens.home.component.ShowCountDown
import com.example.wordle.ui.screens.home.component.ShowRotateScreenMessage
import com.example.wordle.ui.screens.home.component.showGame
import com.example.wordle.ui.theme.getEffectColors
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier, windowSizeClass: WindowSizeClass,
    navController: NavController = rememberNavController()) {

    val height = LocalConfiguration.current.screenHeightDp
    val scope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }

    val viewModel: HomeScreenViewModel = hiltViewModel()
    val state  = viewModel.state

    val settings  = viewModel.settings
    var showSettingsDialog by rememberSaveable { mutableStateOf(false) }
    var showHelpDialog by rememberSaveable { mutableStateOf(false) }

    val countDown  = viewModel.timeInMillis
    var showAnswer by rememberSaveable { mutableStateOf(false) }
    var winningRow by rememberSaveable { mutableStateOf<Int?>(null) }
    var dialogMessage by rememberSaveable { mutableStateOf("") }
    var snackBarMessage by rememberSaveable { mutableStateOf("") }
    var showResetDialog by rememberSaveable { mutableStateOf(false) }

    var win by remember { mutableStateOf(false) }
    var isElevated by remember { mutableStateOf(false) }
    val elevation by animateFloatAsState(targetValue = if (isElevated) 20f else 0f, label = "elevation")
    val rowScale by animateFloatAsState(targetValue = if (isElevated) 1.5f else 1f, label = "rowScale")


    LaunchedEffect(key1 = true) {
        viewModel.events.collectLatest { event ->
            when (event) {
                is HomeScreenUiChannel.GameOver -> { showAnswer = true }
                is HomeScreenUiChannel.NotInWordList -> {
                    scope.launch {
                        snackBarMessage = "Not in word list"
                        snackBarHostState.currentSnackbarData?.dismiss()
                        snackBarHostState.showSnackbar("")
                    }
                }
                is HomeScreenUiChannel.Win -> {
                    showSettingsDialog = false
                    winningRow = event.winningRow
                    dialogMessage = "Correct!"
                    isElevated = true
                    win = true
                    delay(1000)
                    showResetDialog = true
                }

                HomeScreenUiChannel.TimesUp -> {
                    showSettingsDialog = false
                    dialogMessage = "Times up!"
                    showResetDialog = true
                }
            }
        }
    }

    if (showResetDialog) {
        RestartGameDialog(
            title = dialogMessage,
            onRestart = {
                isElevated = false
                win = false
                showResetDialog = false
                viewModel.onEvent(HomeScreenUiEvent.Restart)
        }, onDismiss = { showResetDialog = false })
    }

    if (showSettingsDialog) {
        SettingsDialog(hardMode = settings.value?.hardMode ?: false,
            restartGame = {
                isElevated = false
                win = false
                viewModel.onEvent(HomeScreenUiEvent.Restart)
                showSettingsDialog = false
            },
            onToggleHardMode = {
                if (viewModel.playing.value != true) {
                    viewModel.onEvent(HomeScreenUiEvent.SettingsUiEvent.ToggleHardMode)
                    return@SettingsDialog
                }
                snackBarMessage = "Cannot change hard mode while playing"
                snackBarHostState.currentSnackbarData?.dismiss()
                scope.launch { snackBarHostState.showSnackbar("") }
            } ,
            contrast = settings.value?.contrast ?: false,
            onToggleContrast = { viewModel.onEvent(HomeScreenUiEvent.SettingsUiEvent.ToggleContrast) }) {
            showSettingsDialog = false
        }
    }

    if (showHelpDialog) {
        HelpDialog(contrast = settings.value?.contrast ?: false) { showHelpDialog = false }
    }

    Scaffold(
        topBar = {
            AppTopBar(title = "Wordle", content = {
                IconButton(onClick = { showHelpDialog = true }) {
                    Image(painter = painterResource(id = R.drawable.baseline_help_outline_24),
                        contentDescription = "Help icon",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
                }
                IconButton(onClick = { navController.navigate(Screens.History.route) }) {
                    Image(painter = painterResource(id = R.drawable.baseline_bar_chart_24),
                        contentDescription = "History icon",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
                }
                IconButton(onClick = {
                    showSettingsDialog = true
                    showAnswer = !showAnswer
                }) {
                    Image(painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings icon",
                        colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
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
                        Text(text = snackBarMessage,
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

                        CellRow(modifier = elevateCellRow(winningRow == 1, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.first.submitted, userGuess = state.value.guesses.first.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                            word = state.value.word) {
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(modifier = elevateCellRow(winningRow == 2, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.second.submitted, userGuess = state.value.guesses.second.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                            word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(modifier = elevateCellRow(winningRow == 3, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.third.submitted, userGuess = state.value.guesses.third.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                             word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(modifier = elevateCellRow(winningRow == 4, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.fourth.submitted, userGuess = state.value.guesses.fourth.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                             word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(modifier = elevateCellRow(winningRow == 5, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.fifth.submitted, userGuess = state.value.guesses.fifth.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                             word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }
                        CellRow(modifier = elevateCellRow(winningRow == 6, elevation, rowScale, isElevated, win),
                            submitted = state.value.guesses.sixth.submitted, userGuess = state.value.guesses.sixth.userGuess,
                            contrast = viewModel.settings.value?.contrast ?: false,
                            word = state.value.word){
                            viewModel.onEvent(HomeScreenUiEvent.UpdateCharStatus(it))
                        }

                        Spacer(modifier = Modifier.height(50.dp))

                        KeyBoardSection(
                            charStatus = viewModel.charStatus.value,
                            hardMode = settings.value?.hardMode ?: false,
                            contrast = settings.value?.contrast ?: false,
                            onSubmit = { viewModel.onEvent(HomeScreenUiEvent.Submit) },
                            onClear = { viewModel.onEvent(HomeScreenUiEvent.Clear) }) { char ->
                            viewModel.onEvent(HomeScreenUiEvent.CharSelect(char))
                        }
                    }

                    ShowCountDown(countDown = countDown.value, screenHeight = height)

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
fun SettingsDialog(
    modifier: Modifier = Modifier,
    restartGame: () -> Unit,
    hardMode: Boolean, onToggleHardMode: (Boolean) -> Unit,
    contrast: Boolean, onToggleContrast: (Boolean) -> Unit,
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
                .padding(vertical = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Settings", style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column {
                    Spacer(modifier = Modifier.height(5.dp))
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Hard Mode",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "Any revealed hints must be used in subsequent \nguesses",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Switch(
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = getEffectColors(contrast).isInWord,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            ),
                            checked = hardMode,
                            onCheckedChange = onToggleHardMode)
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }

            }


            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Column {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column{
                            Text(text = "High Contrast Mode",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "Contrast and colorblindness improvements",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Switch(
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = getEffectColors(contrast).isInWord,
                                uncheckedThumbColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            ),
                            checked = contrast,
                            onCheckedChange = onToggleContrast)
                    }

                    Spacer(modifier = Modifier.height(1.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }

            }


            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(5.dp))
                Column {
                    Row(
                        modifier = modifier.fillMaxWidth()
                            .clickable { restartGame() },
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column{
                            Text(text = "Restart Game",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                            )
                            Spacer(modifier = Modifier.height(5.dp))

                            Text(text = "Quit the current game and start a new one",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                            )
                        }

                        Image(painter = painterResource(id = R.drawable.baseline_restart_alt_24),
                            contentDescription = "Restart icon",
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.onBackground))
                    }

                    Spacer(modifier = Modifier.height(5.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f)
                    )
                }

            }
        }
    }
}



