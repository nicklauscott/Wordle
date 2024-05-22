package com.example.wordle.domain.repository

import com.example.wordle.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun saveSettings(settings: Settings)
    suspend fun getSettings(): Flow<List<Settings?>>
}