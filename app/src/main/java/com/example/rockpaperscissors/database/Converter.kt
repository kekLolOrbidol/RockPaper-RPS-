package com.example.rockpaperscissors.database

import androidx.room.TypeConverter
import com.example.rockpaperscissors.model.Game
import java.util.*

class Converter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

    @TypeConverter
    fun resultToString(result: Game.Result): String {
        return result.toString()
    }

    @TypeConverter
    fun stringToResult(value: String): Game.Result? {
        return Game.Result.values().first { it.toString() == value }
    }
}