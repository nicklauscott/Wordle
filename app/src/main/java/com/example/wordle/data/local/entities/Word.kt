package com.example.wordle.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Word(@PrimaryKey val word: String, val used: Boolean = false)
