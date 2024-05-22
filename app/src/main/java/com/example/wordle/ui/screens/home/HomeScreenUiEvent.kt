package com.example.wordle.ui.screens.home

sealed class HomeScreenUiEvent {
    object Submit: HomeScreenUiEvent()
    object Restart: HomeScreenUiEvent()
    object Clear: HomeScreenUiEvent()
    data class CharSelect(val char: Char): HomeScreenUiEvent()
    object History: HomeScreenUiEvent()
    sealed class SettingsUiEvent: HomeScreenUiEvent() {
        object ToggleHardMode: SettingsUiEvent()
        object ToggleContrast: SettingsUiEvent()
    }
    data class UpdateCharStatus(val charStatusList: List<CharStatus>): HomeScreenUiEvent()
}

sealed class HomeScreenUiChannel {
    class Win(val winningRow: Int): HomeScreenUiChannel()
    object GameOver: HomeScreenUiChannel()
    object NotInWordList: HomeScreenUiChannel()
    object TimesUp: HomeScreenUiChannel()
}