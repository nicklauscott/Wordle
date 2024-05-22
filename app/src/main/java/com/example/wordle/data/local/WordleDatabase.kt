package com.example.wordle.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.wordle.data.local.daos.GameRecordDao
import com.example.wordle.data.local.daos.SettingsDao
import com.example.wordle.data.local.daos.WordDao
import com.example.wordle.data.local.entities.GameRecord
import com.example.wordle.data.local.entities.Settings
import com.example.wordle.data.local.entities.Word

@Database(
    entities = [Settings::class, Word::class, GameRecord::class],
    version = 1, exportSchema = false
)
abstract class WordleDatabase: RoomDatabase() {

    abstract val settingsDao: SettingsDao
    abstract val wordDao: WordDao
    abstract val gameRecordDao: GameRecordDao

}