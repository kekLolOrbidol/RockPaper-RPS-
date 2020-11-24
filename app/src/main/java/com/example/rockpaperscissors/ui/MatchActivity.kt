package com.example.rockpaperscissors.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.database.GameRepository
import com.example.rockpaperscissors.model.Game
import kotlinx.android.synthetic.main.match_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchActivity : AppCompatActivity() {
    private val gameList = arrayListOf<Game>()
    private val gameAdapter = GameAdapter(gameList)
    private lateinit var gameRepository: GameRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.match_main)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.history_title)

        gameRepository = GameRepository(this)
        initViews()
    }

    private fun initViews() {
        rvContent.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL, false
        )
        rvContent.adapter = gameAdapter
        rvContent.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        getAllGamesFromDatabase()
    }

    private fun getAllGamesFromDatabase() {
        CoroutineScope(Dispatchers.Main).launch {
            val gameList = withContext(Dispatchers.IO) {
                gameRepository.getAllGames()
            }
            this@MatchActivity.gameList.clear()
            this@MatchActivity.gameList.addAll(gameList)
            this@MatchActivity.gameAdapter.notifyDataSetChanged()
        }
    }

    private fun deleteHistory() {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                gameRepository.deleteAllGames()
            }
            getAllGamesFromDatabase()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_match, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_delete_history -> {
                deleteHistory()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}