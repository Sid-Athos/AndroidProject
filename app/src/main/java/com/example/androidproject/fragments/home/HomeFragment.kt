package com.example.androidproject.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.fragments.GameCardList
import com.example.androidproject.R
import com.example.androidproject.services.SteamApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamApiService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.game_list_view)
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = api.getMostSelledGames().await()
                val games = response.asJsonObject
                    .get("response").asJsonObject
                    .get("ranks").asJsonArray
                    .take(30)
                    .map { rank -> rank.asJsonObject.get("appid").toString() }
                recyclerView.adapter = GameCardList(games, true, this@HomeFragment)
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }
    }
}