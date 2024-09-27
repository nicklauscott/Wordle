package com.example.wordle.ui.screens.history

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.wordle.ui.screens.components.AppTopBar
import com.example.wordle.ui.screens.history.components.GameRecordCell
import com.example.wordle.ui.screens.history.components.GameRecordSummary
import com.example.wordle.ui.screens.history.components.HistoryScreenEvent

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryScreenViewModel) {

    val state = viewModel.state.collectAsState()
    val settings = viewModel.settings.value
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Stats & History",
                icon = Icons.AutoMirrored.Filled.ArrowBack
            ) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->

        when {
            state.value.isEmpty() -> {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(text = "No Records Found",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(text = "Complete a game to see your stats here.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        fontSize = 15.sp,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .padding(top = paddingValues.calculateTopPadding(),
                            bottom = paddingValues.calculateBottomPadding())
                ) {

                    Column {
                        GameRecordSummary(
                            contrast = settings?.contrast ?: false,
                            gameRecords = state.value
                        ) {
                            viewModel.onEvent(HistoryScreenEvent.ResetStats)
                        }
                    }

                    Spacer(Modifier.height(25.dp))

                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp).padding(top = 16.dp)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.45f)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "History", style = MaterialTheme.typography.bodyLarge,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.onBackground)
                    }

                    LazyRow {
                        items(state.value) { gameRecord ->
                            GameRecordCell(
                                gameNo = state.value.indexOf(gameRecord),
                                contrast = settings?.contrast ?: false,
                                gameRecord = gameRecord
                            )
                        }
                    }
                }
            }
        }
    }
}