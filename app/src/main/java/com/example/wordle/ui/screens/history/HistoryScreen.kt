package com.example.wordle.ui.screens.history

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.wordle.ui.screens.components.AppTopBar

@Composable
fun HistoryScreen(navController: NavController) {

    Scaffold(
        topBar = {
            AppTopBar(
                title = "History",
                icon = Icons.AutoMirrored.Filled.ArrowBack,
                content = {
                    IconButton(onClick = {  }) {
                        Icon(imageVector = Icons.Default.Settings, contentDescription = "settings icon")
                    }
                }
            ) {
                navController.popBackStack()
            }
        }
    ) { paddingValues ->

       paddingValues.calculateTopPadding()
    }
}