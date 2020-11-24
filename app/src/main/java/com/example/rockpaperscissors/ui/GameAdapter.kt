package com.example.rockpaperscissors.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rockpaperscissors.R
import com.example.rockpaperscissors.model.Game
import com.example.rockpaperscissors.model.Game.Result.*

class GameAdapter(private val games: List<Game>) :
    RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.match_item, parent, false)
        )
    }

    override fun getItemCount(): Int = games.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(games[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val context: Context = itemView.context.applicationContext
        private val ivComputer: ImageView = itemView.findViewById(R.id.ivComputer)
        private val ivPlayer: ImageView = itemView.findViewById(R.id.ivPlayer)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvResult: TextView = itemView.findViewById(R.id.tvResult)

        fun bind(game: Game) {

            when {
                game.result == WIN -> tvResult.text = context.getString(R.string.win)
                game.result == DRAW -> tvResult.text = context.getString(R.string.draw)
                else -> tvResult.text = context.getString(R.string.lose)
            }

            ivComputer.setImageDrawable(itemView.context.applicationContext.getDrawable(game.computer_hand))
            ivPlayer.setImageDrawable(itemView.context.applicationContext.getDrawable(game.player_hand))
            tvDate.text = game.date.toString()
        }
    }
}