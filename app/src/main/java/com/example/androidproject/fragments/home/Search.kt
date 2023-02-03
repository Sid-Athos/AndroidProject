package com.example.androidproject.fragments.home

import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.fragments.GameCardList
import com.example.androidproject.services.SteamCommunityService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Search: Fragment(R.layout.fragment_search) {
    private val api = Retrofit.Builder()
        .baseUrl("https://steamcommunity.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamCommunityService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    fun bindSearch(input: String) {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.recycler_view)
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { api.searchApps(input).await() }
            val games = response.asJsonArray.map { app -> app.asJsonObject.get("appid").asString }
            Log.v("home fragment input", games.toString())
            withContext(Dispatchers.Main) {
                recyclerView.adapter = GameCardList(games, R.id.go_to_details2, this@Search)
            }
        }
    }
}