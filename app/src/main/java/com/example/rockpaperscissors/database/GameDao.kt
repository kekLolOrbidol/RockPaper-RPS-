package com.example.rockpaperscissors.database

import androidx.room.*
import com.example.rockpaperscissors.model.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM game_table")
    suspend fun getAllGames(): List<Game>

    @Insert
    suspend fun insertGame(game: Game)

    @Delete
    suspend fun deleteGame(game: Game)

    @Update
    suspend fun updateGame(game: Game)

    @Query("DELETE FROM game_table")
    suspend fun deleteAllGames()

    @Query("SELECT COUNT(result) FROM game_table WHERE result = \"WIN\"")
    suspend fun getWins(): Int

    @Query("SELECT COUNT(result) FROM game_table WHERE result = \"DRAW\"")
    suspend fun getDraws(): Int

    @Query("SELECT COUNT(result) FROM game_table WHERE result = \"LOSE\"")
    suspend fun getLosses(): Int
}