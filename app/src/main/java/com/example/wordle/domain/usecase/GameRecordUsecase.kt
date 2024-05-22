package com.example.wordle.domain.usecase

import com.example.wordle.domain.usecase.record.GetGameRecord
import com.example.wordle.domain.usecase.record.GetGameRecords
import com.example.wordle.domain.usecase.record.SaveGameRecord
import javax.inject.Inject

class GameRecordUsecase @Inject constructor(
    val getGameRecord: GetGameRecord,
    val getGameRecords: GetGameRecords,
    val saveGameRecord: SaveGameRecord
)