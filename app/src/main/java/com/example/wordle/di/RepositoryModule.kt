package com.example.wordle.di

import com.example.wordle.data.repository.GameRepositoryImpl
import com.example.wordle.data.repository.SettingsRepositoryImpl
import com.example.wordle.data.repository.WordRepositoryImpl
import com.example.wordle.domain.repository.GameRecordRepository
import com.example.wordle.domain.repository.SettingsRepository
import com.example.wordle.domain.repository.WordRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSettingRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindWordRepository(
        wordRepositoryImpl: WordRepositoryImpl
    ): WordRepository

    @Binds
    @Singleton
    abstract fun bindGameRecordRepository(
        gameRepositoryImpl: GameRepositoryImpl
    ): GameRecordRepository
}

