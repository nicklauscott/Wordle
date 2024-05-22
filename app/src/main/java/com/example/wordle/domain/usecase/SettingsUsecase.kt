package com.example.wordle.domain.usecase

import com.example.wordle.domain.usecase.settings.GetSettings
import com.example.wordle.domain.usecase.settings.SaveSettings
import javax.inject.Inject

data class SettingsUsecase @Inject constructor(
    val getSettings: GetSettings,
    val saveSettings: SaveSettings
)