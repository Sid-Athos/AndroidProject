package com.example.androidproject.fragments.details

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.example.androidproject.services.SteamStoreService
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GameDetailsFragment: Fragment(R.layout.fragment_game_details) {
    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamStoreService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = arguments?.getString("gameId") ?: "10"
        val game = (requireActivity() as MainActivity).cache[gameId]

        if (game == null) {
            GlobalScope.launch(Dispatchers.IO) {
                val data: JsonElement
                try {
                    data = api.getAppById(gameId).await()

                    if (data.asJsonObject.get(gameId).asJsonObject.get("success").asBoolean) {
                        val newGame = Game(gameId, data)
                        (requireActivity() as MainActivity).cache[gameId] = newGame

                        withContext(Dispatchers.Main) {
                            bind(newGame)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("game list", "error getting game data", e)
                }
            }
        } else bind(game)
    }

    private fun bind(game: Game) {
        requireView().findViewById<TextView>(R.id.title).text = game.title
        requireView().findViewById<TextView>(R.id.studio).text = game.studio
        requireView().findViewById<TextView>(R.id.description).text = game.shortDescription
    }
}