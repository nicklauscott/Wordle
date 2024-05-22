package com.example.wordle.domain.usecase.settings

import com.example.wordle.domain.repository.SettingsRepository
import javax.inject.Inject

class GetSettings @Inject constructor(private val repository: SettingsRepository) {
    suspend operator fun invoke() = repository.getSettings()
}