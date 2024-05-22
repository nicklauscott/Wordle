package com.example.wordle.ui.screens.home

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.domain.model.GameRecord
import com.example.wordle.domain.usecase.GameRecordUsecase
import com.example.wordle.domain.usecase.WordUsecase
import com.example.wordle.ui.screens.home.component.HomeScreenUiChannel
import com.example.wordle.ui.screens.home.component.HomeScreenUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val gameRecordUsecase: GameRecordUsecase,
    private val wordUsecase: WordUsecase
) : ViewModel() {

    private var _state: MutableState<HomeScreenUIState> = mutableStateOf(HomeScreenUIState())
    val state: State<HomeScreenUIState> = _state

    private val channel = Channel<HomeScreenUiChannel>(Channel.BUFFERED)
    val events = channel.receiveAsFlow()

    private val _charStatus: MutableState<List<CharStatus>> = mutableStateOf(listOf())
    val charStatus: State<List<CharStatus>> = _charStatus

    var playing = mutableStateOf<Boolean?>(null)
        private set

    private var countDown = 300

    init {
        viewModelScope.launch {
            val words = viewModelScope.async(Dispatchers.IO) { wordUsecase.getWord() }
            _state.value = state.value.copy(word = words.await(), attempts = 1)
            playing.value = true
        }
    }

    fun onEvent(event: HomeScreenUiEvent) {
        if (playing.value == true) {
            when (event) {
                is HomeScreenUiEvent.CharSelect ->  addChar(event.char)
                is HomeScreenUiEvent.Clear -> removeChar()
                is HomeScreenUiEvent.History -> TODO()
                is HomeScreenUiEvent.Settings -> TODO()
                is HomeScreenUiEvent.Submit -> submit()
                is HomeScreenUiEvent.UpdateCharStatus -> updateCharStatus(event.charStatusList)
                is HomeScreenUiEvent.CountDown -> countDown = event.duration
                else -> {}
            }
            return
        }

        when (event) {
            is HomeScreenUiEvent.Restart -> resetGame()
            else -> {}
        }
    }

    private fun resetGame() {
        viewModelScope.launch {
            playing.value = null
            val words = viewModelScope.async(Dispatchers.IO) { wordUsecase.getWord() }
            _state.value = HomeScreenUIState(word = words.await(), attempts = 1)
            countDown = 300
            _charStatus.value = emptyList()
            playing.value = true
        }
    }

    private fun updateCharStatus(charStatusList: List<CharStatus>) {
        val newUsedChars = charStatus.value.map { it.char }
        val tempList = mutableListOf<CharStatus>()
        charStatusList.forEach { charStatus ->
            if (!newUsedChars.contains(charStatus.char)) {
                tempList.add(charStatus)
            }
        }
        _charStatus.value = charStatus.value + tempList
    }

    private fun newString(oldString: String): String{
        var index = 0
        for (char in oldString) {
            if (char != ' ') {
                index++
            }else {
                break
            }
        }
        val newString = oldString.map { it }.toMutableList()
        newString.removeAt(index - 1)
        newString.add(' ')
        return newString.joinToString("")
    }

    private fun removeChar() {
        when (state.value.attempts) {
            1 -> {
                if (state.value.guesses.first.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.first.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(first = state.value.guesses.first.copy(userGuess = oldCharSet))
                    )
                }
            }
            2 -> {
                if (state.value.guesses.second.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.second.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(second = state.value.guesses.second.copy(userGuess = oldCharSet))
                    )
                }
            }
            3 -> {
                if (state.value.guesses.third.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.third.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(third = state.value.guesses.third.copy(userGuess = oldCharSet))
                    )
                }
            }
            4 -> {
                if (state.value.guesses.fourth.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.fourth.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(fourth = state.value.guesses.fourth.copy(userGuess = oldCharSet))
                    )
                }
            }
            5 -> {
                if (state.value.guesses.fifth.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.fifth.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(fifth = state.value.guesses.fifth.copy(userGuess = oldCharSet))
                    )
                }
            }
            6 -> {
                if (state.value.guesses.sixth.userGuess.trim().isNotEmpty()) {
                    val oldCharSet = newString(state.value.guesses.sixth.userGuess)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(sixth = state.value.guesses.sixth.copy(userGuess = oldCharSet))
                    )
                }
            }
        }
    }

    private fun submit() {
        viewModelScope.launch(Dispatchers.IO) {
            when (state.value.attempts) {
                1 -> {
                    if (state.value.guesses.first.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.first.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(first = state.value.guesses.first.copy(submitted = true))
                        )
                        if (state.value.guesses.first.userGuess == state.value.word) {
                            saveGameRecord(true)
                            playing.value = false
                            channel.send(HomeScreenUiChannel.Win)
                        }
                    }
                }
                2 -> {
                    if (state.value.guesses.second.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.second.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(second = state.value.guesses.second.copy(submitted = true))
                        )
                        if (state.value.guesses.second.userGuess == state.value.word) {
                            saveGameRecord(true)
                            playing.value = false
                            channel.send(HomeScreenUiChannel.Win)
                        }
                    }
                }
                3 -> {
                    if (state.value.guesses.third.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.third.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(third = state.value.guesses.third.copy(submitted = true))
                        )
                        if (state.value.guesses.third.userGuess == state.value.word) {
                            saveGameRecord(true)
                            playing.value = false
                            channel.send(HomeScreenUiChannel.Win)
                        }
                    }
                }
                4 -> {
                    if (state.value.guesses.fourth.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.fourth.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(fourth = state.value.guesses.fourth.copy(submitted = true))
                        )
                        if (state.value.guesses.fourth.userGuess == state.value.word) {
                            saveGameRecord(true)
                            playing.value = false
                            channel.send(HomeScreenUiChannel.Win)
                        }
                    }
                }
                5 -> {
                    if (state.value.guesses.fifth.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.fifth.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(fifth = state.value.guesses.fifth.copy(submitted = true))
                        )
                        if (state.value.guesses.fifth.userGuess == state.value.word) {
                            saveGameRecord(true)
                            playing.value = false
                            channel.send(HomeScreenUiChannel.Win)
                        }
                    }
                }
                6 -> {
                    if (state.value.guesses.sixth.userGuess.trim().length == 5) {
                        if (!wordUsecase.getWord.doesWordExist(state.value.guesses.sixth.userGuess)) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.NotInWordList) }
                            return@launch
                        }
                        _state.value = state.value.copy(
                            attempts = state.value.attempts + 1,
                            guesses = state.value.guesses.copy(sixth = state.value.guesses.sixth.copy(submitted = true))
                        )
                        if (state.value.guesses.sixth.userGuess != state.value.word) {
                            viewModelScope.launch { channel.send(HomeScreenUiChannel.GameOver) }
                            saveGameRecord(false)
                            return@launch
                        }

                        saveGameRecord(true)
                        playing.value = false
                        channel.send(HomeScreenUiChannel.Win)
                    }
                }
            }
        }
    }

    private fun addChar(char: Char) {
        when (state.value.attempts) {
            1 -> {
                if (state.value.guesses.first.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.first.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(first = state.value.guesses.first.copy(userGuess = oldCharSet))
                    )
                }
            }
            2 -> {
                if (state.value.guesses.second.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.second.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(second = state.value.guesses.second.copy(userGuess = oldCharSet))
                    )
                }
            }
            3 -> {
                if (state.value.guesses.third.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.third.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(third = state.value.guesses.third.copy(userGuess = oldCharSet))
                    )
                }
            }
            4 -> {
                if (state.value.guesses.fourth.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.fourth.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(fourth = state.value.guesses.fourth.copy(userGuess = oldCharSet))
                    )
                }
            }
            5 -> {
                if (state.value.guesses.fifth.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.fifth.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(fifth = state.value.guesses.fifth.copy(userGuess = oldCharSet))
                    )
                }
            }
            6 -> {
                if (state.value.guesses.sixth.userGuess.trim().length < 5) {
                    val oldCharSet = state.value.guesses.sixth.userGuess.replaceFirst(' ', char)
                    _state.value = state.value.copy(
                        guesses = state.value.guesses.copy(sixth = state.value.guesses.sixth.copy(userGuess = oldCharSet))
                    )
                }
            }
        }
    }

    private suspend fun saveGameRecord(win: Boolean) {
        val guesses = listOf(
            state.value.guesses.first.userGuess, state.value.guesses.second.userGuess,
            state.value.guesses.third.userGuess, state.value.guesses.fourth.userGuess,
            state.value.guesses.fifth.userGuess, state.value.guesses.sixth.userGuess
        )
        val gameRecord = GameRecord(
            word = state.value.word,
            attempts = guesses,
            durationInSeconds = 300 - countDown,
            score = if (win) when (state.value.attempts) {
                1 -> 6 2 -> 5 3 -> 4 4 -> 3 5 -> 2 6 -> 1 else -> 0
            } else 0
        )

        gameRecordUsecase.saveGameRecord(gameRecord)
    }

    override fun onCleared() {
        viewModelScope.launch(Dispatchers.IO) { saveGameRecord(false) }
        super.onCleared()
    }

}




