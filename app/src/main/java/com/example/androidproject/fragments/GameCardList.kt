package com.example.androidproject.fragments

import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.services.SteamApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.androidproject.R
import com.example.androidproject.models.Game
import kotlinx.coroutines.*

class GameCardList(private val games: List<String>, private val displayDetails: Boolean): RecyclerView.Adapter<GameCardList.ViewHolder>() {
    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamApiService::class.java)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleView: TextView
        private val studioNameView: TextView
        private val priceView: TextView

        init {
            titleView = itemView.findViewById(R.id.game_title)
            studioNameView = itemView.findViewById(R.id.game_studio_name)
            priceView = itemView.findViewById(R.id.game_price)
        }

        fun bind(data: Game) {
            titleView.text = data.title
            studioNameView.text = data.studio
            priceView.text = itemView.resources.getString(R.string.gamePrice, data.price)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_game_card, viewGroup, false)
        return ViewHolder(view)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameId = games[position]

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) { api.getAppById(gameId).await() }
                holder.bind(Game(gameId, data))
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}