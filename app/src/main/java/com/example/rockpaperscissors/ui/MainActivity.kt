package com.example.rockpaperscissors.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.repository.LinksUtills
import com.example.rockpaperscissors.repository.ResponseApi
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.coroutines.*
import java.util.*

class MainActivity : AppCompatActivity(), ResponseApi {

    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        window.statusBarColor = Color.GRAY
        toolbar.visibility = View.GONE
        val link = LinksUtills(this)
        link.attachWeb(this)
        gameRepository = GameRepository(this@MainActivity)
        GlobalScope.launch(Dispatchers.IO){
            Thread.sleep(5000)
            withContext(Dispatchers.Main){
                progress_bar.visibility = View.GONE
                toolbar.visibility = View.VISIBLE
                main_content.visibility = View.VISIBLE
                initViews()
            }
        }
    }

    private fun initViews() {
        ibRock.setOnClickListener { onPlayerChoice(Game.Choice.ROCK) }
        ibPaper.setOnClickListener { onPlayerChoice(Game.Choice.PAPER) }
        ibScissors.setOnClickListener { onPlayerChoice(Game.Choice.SCISSORS) }
        getStatistics()
    }

    private fun onPlayerChoice(playerHand: Game.Choice) {
        val computerHand = Game.Choice.values().random()

        ivComputer.setImageDrawable(getDrawable(getChoiceDrawableId(computerHand)))
        ivPlayer.setImageDrawable(getDrawable(getChoiceDrawableId(playerHand)))

        val game = Game(
            date = Date(),
            player_hand = getChoiceDrawableId(playerHand),
            computer_hand = getChoiceDrawableId(computerHand),
            result = calculateResult(playerHand, computerHand)
        )

        if (game.result == Game.Result.WIN) {
            tvResult.text = getString(R.string.win)
        } else if (game.result == Game.Result.DRAW) {
            tvResult.text = getString(R.string.draw)
        } else {
            tvResult.text = getString(R.string.lose)
        }
        addGameToDatabase(game)
    }

    private fun calculateResult(playerHand: Game.Choice, computerHand: Game.Choice): Game.Result {
        if (playerHand == computerHand) {
            return Game.Result.DRAW
        }

        return if (
            playerHand == Game.Choice.ROCK && computerHand == Game.Choice.SCISSORS ||
            playerHand == Game.Choice.PAPER && computerHand == Game.Choice.ROCK ||
            playerHand == Game.Choice.SCISSORS && computerHand == Game.Choice.PAPER
        ) {
            Game.Result.WIN
        } else Game.Result.LOSE
    }

    private fun getChoiceDrawableId(choice: Game.Choice): Int {
        return when (choice) {
            Game.Choice.ROCK -> R.drawable.rock
            Game.Choice.PAPER -> R.drawable.paper
            Game.Choice.SCISSORS -> R.drawable.scissors
        }
    }

    private fun addGameToDatabase(game: Game) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.insertGame(game)
                getStatistics()
            }
        }
    }

    private fun getStatistics() {
        CoroutineScope(Dispatchers.Main).launch {
            var wins = 0
            var draws = 0
            var losses = 0
            withContext(Dispatchers.IO) {
                wins = gameRepository.getWins()
                draws = gameRepository.getDraws()
                losses = gameRepository.getLosses()
            }
            tvScore.text = getString(R.string.stats, wins, draws, losses)
        }
    }

    private fun startHistoryActivity() {
        val intent = Intent(this, MatchActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getStatistics()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_show_match -> {
                startHistoryActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun callResponse(url: String) {
        val builder = CustomTabsIntent.Builder()
        builder.setToolbarColor(ContextCompat.getColor(this, R.color.black))
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
        finish()
    }
}
