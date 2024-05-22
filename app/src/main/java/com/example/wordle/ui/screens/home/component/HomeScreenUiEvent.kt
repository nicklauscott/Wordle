package com.example.wordle.ui.screens.home.component

import com.example.wordle.ui.screens.home.CharStatus

sealed class HomeScreenUiEvent {
    object Submit: HomeScreenUiEvent()
    data class CountDown(val duration: Int): HomeScreenUiEvent()
    object Restart: HomeScreenUiEvent()
    object Clear: HomeScreenUiEvent()
    data class CharSelect(val char: Char): HomeScreenUiEvent()
    object History: HomeScreenUiEvent()
    object Settings: HomeScreenUiEvent()
    data class UpdateCharStatus(val charStatusList: List<CharStatus>): HomeScreenUiEvent()
}

sealed class HomeScreenUiChannel {
    object Win: HomeScreenUiChannel()
    object GameOver: HomeScreenUiChannel()
    object NotInWordList: HomeScreenUiChannel()
}