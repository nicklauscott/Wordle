package com.example.wordle.ui.screens.history.components

sealed interface HistoryScreenEvent {
    object ResetStats: HistoryScreenEvent
}