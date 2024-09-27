package com.example.wordle.ui.screens.history

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.domain.model.GameRecord
import com.example.wordle.domain.model.Settings
import com.example.wordle.domain.usecase.GameRecordUsecase
import com.example.wordle.domain.usecase.SettingsUsecase
import com.example.wordle.ui.screens.history.components.HistoryScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryScreenViewModel @Inject constructor(private val gameRecord: GameRecordUsecase, private val settingsUseCase: SettingsUsecase): ViewModel() {

    private val _settings: MutableState<Settings?> = mutableStateOf(null)
    val settings: State<Settings?> = _settings

     val state = gameRecord.getGameRecords()
         .map { it.sortedByDescending { gameRecord -> gameRecord.date } }
         .stateIn(
         viewModelScope,
         SharingStarted.WhileSubscribed(5000),
         emptyList()
     )

    init {
        viewModelScope.launch {
            val settings = viewModelScope.async(Dispatchers.IO) { settingsUseCase.getSettings() }
            _settings.value = settings.await().first().first()
        }
    }

    fun onEvent(event: HistoryScreenEvent) {
        when (event) {
            HistoryScreenEvent.ResetStats -> {
                viewModelScope.launch(Dispatchers.IO) {
                    gameRecord.resetGameRecords()
                }
            }
        }
    }


}