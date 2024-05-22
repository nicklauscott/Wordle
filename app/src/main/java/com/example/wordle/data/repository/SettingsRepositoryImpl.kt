package com.example.wordle.data.repository

import com.example.wordle.data.local.daos.SettingsDao
import com.example.wordle.data.local.entities.Settings
import com.example.wordle.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
): SettingsRepository {
    override suspend fun saveSettings(settings: Settings) {
        settingsDao.insertSetting(settings)
    }

    override suspend fun getSettings(): Flow<List<Settings>> {
        return settingsDao.getSettings()
    }
}