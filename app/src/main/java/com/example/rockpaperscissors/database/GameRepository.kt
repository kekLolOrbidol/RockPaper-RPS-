package com.example.rockpaperscissors.database

import android.content.Context
import com.example.rockpaperscissors.model.Game

class GameRepository(context: Context) {

    private val gameDao: GameDao

    init {
        val database = GameRoomDatabase.getDatabase(context)
        gameDao = database!!.gameDao()
    }


    suspend fun getAllGames(): List<Game> {
        return gameDao.getAllGames()
    }


    suspend fun insertGame(game: Game) {
        return gameDao.insertGame(game)
    }


    suspend fun deleteGame(game: Game) {
        return gameDao.deleteGame(game)
    }


    suspend fun updateGame(game: Game) {
        return gameDao.updateGame(game)
    }


    suspend fun deleteAllGames() {
        gameDao.deleteAllGames()
    }

    suspend fun getWins(): Int {
        return  gameDao.getWins()
    }

    suspend fun getDraws(): Int {
        return gameDao.getDraws()
    }

    suspend fun getLosses(): Int {
        return gameDao.getLosses()
    }
}