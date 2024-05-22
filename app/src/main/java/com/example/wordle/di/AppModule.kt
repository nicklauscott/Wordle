package com.example.wordle.di

import android.content.Context
import androidx.room.Room
import com.example.wordle.data.local.WordleDatabase
import com.example.wordle.domain.repository.GameRecordRepository
import com.example.wordle.domain.repository.SettingsRepository
import com.example.wordle.domain.repository.WordRepository
import com.example.wordle.domain.usecase.GameRecordUsecase
import com.example.wordle.domain.usecase.SettingsUsecase
import com.example.wordle.domain.usecase.SplashScreeUsecase
import com.example.wordle.domain.usecase.WordUsecase
import com.example.wordle.domain.usecase.record.GetGameRecord
import com.example.wordle.domain.usecase.record.GetGameRecords
import com.example.wordle.domain.usecase.record.SaveGameRecord
import com.example.wordle.domain.usecase.settings.GetSettings
import com.example.wordle.domain.usecase.settings.SaveSettings
import com.example.wordle.domain.usecase.word.GetWord
import com.example.wordle.domain.usecase.word.InsertWord
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): WordleDatabase {
        return Room.databaseBuilder(
            appContext, WordleDatabase::class.java, "wordle_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideWordDao(appDatabase: WordleDatabase) = appDatabase.wordDao

    @Provides
    @Singleton
    fun provideSettingsDao(appDatabase: WordleDatabase) = appDatabase.settingsDao

    @Provides
    @Singleton
    fun provideGameRecordDao(appDatabase: WordleDatabase) = appDatabase.gameRecordDao

    @Provides
    @Singleton
    fun provideGetWord(wordRepository: WordRepository) = GetWord(wordRepository)

    @Provides
    @Singleton
    fun provideInsertWord(wordRepository: WordRepository) = InsertWord(wordRepository)

    @Provides
    @Singleton
    fun provideGetWordUsecase(getWord: GetWord) = WordUsecase(getWord)

    @Provides
    @Singleton
    fun provideSplashUsecase(getWord: GetWord, insertWord: InsertWord) =
        SplashScreeUsecase(getWord, insertWord)

    @Provides
    @Singleton
    fun provideGetSettings(settingsRepository: SettingsRepository) = GetSettings(settingsRepository)

    @Provides
    @Singleton
    fun provideSaveSettings(settingsRepository: SettingsRepository) = SaveSettings(settingsRepository)

    @Provides
    @Singleton
    fun provideSettingsUsecase(getSettings: GetSettings, saveSettings: SaveSettings) =
        SettingsUsecase(getSettings, saveSettings)


    @Provides
    @Singleton
    fun provideGetGameRecord(gameRecordRepository: GameRecordRepository) = GetGameRecord(gameRecordRepository)

    @Provides
    @Singleton
    fun provideGetGameRecords(gameRecordRepository: GameRecordRepository) = GetGameRecords(gameRecordRepository)

    @Provides
    @Singleton
    fun provideSaveGameRecord(gameRecordRepository: GameRecordRepository) = SaveGameRecord(gameRecordRepository)

    @Provides
    @Singleton
    fun provideGameRecordUsecase(
        getGameRecord: GetGameRecord,
        getGameRecords: GetGameRecords,
        saveGameRecord: SaveGameRecord) =
        GameRecordUsecase(getGameRecord, getGameRecords, saveGameRecord)


}