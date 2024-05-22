package com.example.wordle.data.mapper

import com.example.wordle.data.local.entities.Settings

fun Settings.toSettings(): com.example.wordle.domain.model.Settings {
    return com.example.wordle.domain.model.Settings(
        settingsId, username, hardMode, contrast)
}

fun com.example.wordle.domain.model.Settings.toSettings(): Settings {
    return Settings(settingsId, username, hardMode, contrast)
}