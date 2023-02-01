package com.example.androidproject.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.services.SteamApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import com.example.androidproject.R

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

        init {
            titleView = itemView.findViewById(R.id.game_title)
            studioNameView = itemView.findViewById(R.id.game_studio_name)
        }

        fun bind(title: String, studioName: String) {
            titleView.setText(title)
            studioNameView.setText(studioName)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_game_card, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameId = games[position]

        GlobalScope.launch(Dispatchers.Main) {
            try {
                val data = withContext(Dispatchers.IO) { api.getAppById(gameId).await() }
                val title = data.asJsonObject.get(gameId).asJsonObject.get("data").asJsonObject.get("name")
                Log.v("Game Card Bind:", data.toString())
                Log.v("Game Card Bind:", title.toString())
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }

        holder.bind(games[position], "non")
    }

    override fun getItemCount(): Int {
        return games.size
    }
}