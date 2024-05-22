package com.example.wordle.data.repository

import com.example.wordle.data.local.daos.SettingsDao
import com.example.wordle.data.mapper.toSettings
import com.example.wordle.domain.model.Settings
import com.example.wordle.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDao: SettingsDao
): SettingsRepository {
    override suspend fun saveSettings(settings: Settings) {
        settingsDao.insertSetting(settings.toSettings())
    }

    override suspend fun getSettings(): Flow<List<Settings?>> {
        return settingsDao.getSettings().map { it.map { setting -> setting?.toSettings() } }
    }
}