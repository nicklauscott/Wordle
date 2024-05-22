package com.example.wordle.ui.screens.spalsh

import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.domain.WordListUseCase
import com.example.wordle.domain.usecase.SplashScreeUsecase
import com.example.wordle.domain.usecase.WordUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val splashScreeUsecase: SplashScreeUsecase
) : ViewModel() {
    private val wordListUseCase = WordListUseCase()

    val word = wordListUseCase().random()
    private var filteredWords: List<String> = wordListUseCase.getWords(99).toMutableList()

    private var _guessedWords: MutableState<List<String>> = mutableStateOf("      ".map { "      " }.toMutableList())
    val guessedWords: State<List<String>> = _guessedWords

    private var _attempts: MutableIntState = mutableIntStateOf(1)
    val attempts: State<Int> = _attempts

    private val channel = Channel<SplashScreenUiChannel>(Channel.BUFFERED)
    val events = channel.receiveAsFlow()

    init {
        loadWords()

        if (!filteredWords.contains(word)) {
            filteredWords = (filteredWords + word).shuffled()
        }
    }

    private fun loadWords() {
        viewModelScope.launch {
            val isNotEmpty = splashScreeUsecase.getWord.count() > 0
            if (isNotEmpty) {
                channel.trySend(SplashScreenUiChannel.SaveWords(true))
                channel.trySend(SplashScreenUiChannel.GameOver)
            }

            splashScreeUsecase.insertWord().also {
                channel.trySend(SplashScreenUiChannel.SaveWords(true))
                channel.trySend(SplashScreenUiChannel.GameOver)
            }
        }
    }

    fun onEvent(event: SplashScreenUiEvent) {
        when (event) {
            is SplashScreenUiEvent.Submit -> guess(event.guessFeedBack)
        }
    }

    private fun guess(guessFeedBacks: List<GuessFeedBack>?) {
        if (attempts.value >= 6) {
            channel.trySend(SplashScreenUiChannel.GameOver)
            return
        }

        if (guessFeedBacks == null) {
            val chooseWord = filteredWords.random()
            val newGuessedWords = guessedWords.value.toMutableList().also {
                it.removeAt(attempts.value - 1)
                it.add(attempts.value - 1, chooseWord)
            }
            _guessedWords.value = newGuessedWords
            checkGuess(chooseWord)
            return
        }

        val inRightSpot = guessFeedBacks.filter { it.status == CharStatus.IN_RIGHT_SPOT }
        val inWord = guessFeedBacks.filter { it.status == CharStatus.IN_WORD }.map { it.char }.joinToString()
        val notInWord = guessFeedBacks.filter { it.status == CharStatus.NOT_IN_WORD }.map { it.char }.joinToString()
        val newGuess = filteredWords.asSequence()
            .filterNot { char -> char.contains(notInWord, ignoreCase = true) }
            .filter { it.contains(inWord, ignoreCase = true)  }
            .filter { word ->
                inRightSpot.all { feedback ->
                    isCharAtPosition(word, feedback.char, feedback.position)
                }
            }
            .toList()
        filteredWords = newGuess

        if (newGuess.isNotEmpty()) {
            if (newGuess.size == 1) {
                channel.trySend(SplashScreenUiChannel.Guess(newGuess[0]))
            }else {
                channel.trySend(SplashScreenUiChannel.Guess(newGuess.random()))
            }
            return
        }

        channel.trySend(SplashScreenUiChannel.GameOver)
    }

    private fun checkGuess(guess: String) {
        if (guess != word) {
            _attempts.intValue = attempts.value + 1
            channel.trySend(SplashScreenUiChannel.GuessFeedback(createFeedBack(guess, word)))
            filteredWords = filteredWords.filterNot { it == guess }
            return
        }
        channel.trySend(SplashScreenUiChannel.WIn)
    }

    private fun createFeedBack(guess: String, word: String): List<GuessFeedBack> {
        val feedback = mutableListOf<GuessFeedBack>()
        guess.forEach { char ->
            val isInRightPosition = char  == word[guess.indexOf(char)]
            val isInWord = char in word
            val isNotInWord = char !in word

            val status = if (isInRightPosition) CharStatus.IN_RIGHT_SPOT
            else if (isInWord) CharStatus.IN_WORD
            else if (isNotInWord) CharStatus.NOT_IN_WORD else CharStatus.NOT_IN_WORD

            feedback.add(GuessFeedBack(char, status, guess.indexOf(char)))
        }
        return feedback
    }

    private fun isCharAtPosition(word: String, char: Char, position: Int): Boolean {
        if (position < 0 || position >= word.length) {
            return false
        }
        return word[position] == char
    }

    sealed class SplashScreenUiChannel {
        data class GuessFeedback(val feedback: List<GuessFeedBack>) : SplashScreenUiChannel()
        data class Guess(val guess: String) : SplashScreenUiChannel()
        object WIn: SplashScreenUiChannel()
        object GameOver: SplashScreenUiChannel()
        data class SaveWords(val isNotEmpty: Boolean): SplashScreenUiChannel()
    }

}