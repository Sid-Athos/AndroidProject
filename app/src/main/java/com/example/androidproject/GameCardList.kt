package com.example.androidproject

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GameCardList(private val games: List<String>, private val displayDetails: Boolean): RecyclerView.Adapter<GameCardList.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView

        init {
            titleView = itemView.findViewById(R.id.game_title)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_game_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.titleView.setText(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }
}