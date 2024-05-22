package com.example.wordle.domain.usecase.settings

import com.example.wordle.data.local.entities.Settings
import com.example.wordle.domain.repository.SettingsRepository
import javax.inject.Inject

class SaveSettings @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke(settings: Settings) = repository.saveSettings(settings)
}