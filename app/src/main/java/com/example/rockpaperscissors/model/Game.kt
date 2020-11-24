package com.example.rockpaperscissors.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "game_table")
data class Game(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "player_hand")
    var player_hand: Int,

    @ColumnInfo(name = "computer_hand")
    var computer_hand: Int,

    @ColumnInfo(name = "date")
    var date: Date,

    @ColumnInfo(name = "result")
    var result: Result

) : Parcelable {
    enum class Result {
        WIN,
        DRAW,
        LOSE
    }

    enum class Choice {
        ROCK,
        PAPER,
        SCISSORS
    }
}