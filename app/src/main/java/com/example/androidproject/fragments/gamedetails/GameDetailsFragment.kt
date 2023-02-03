package com.example.androidproject.fragments.gamedetails

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidproject.R
import com.example.androidproject.services.ReviewsService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GameDetailsFragment : Fragment(R.layout.fragment_game_details) {
    private val api = Retrofit.Builder()
        .baseUrl("https://api.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ReviewsService::class.java)
        @OptIn(DelicateCoroutinesApi::class)
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            val fragment = GameDetailsFragment()
            val gameTextDetails: TextView = fragment.requireView().findViewById(R.id.game_details_text)
            val gameLogo: ImageView = fragment.requireView().findViewById(R.id.game_details_logo)
            val gameCover: ImageView = fragment.requireView().findViewById(R.id.game_details_cover)
            val gameTextDetailsValue= arguments?.getString("gameDetails")
            val gameLogoUrlValue= arguments?.getString("gameLogo")
            val gameCoverValue= arguments?.getString("gameCover")
            val gameId = arguments?.getString("gameId")
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val response = gameId?.let { api.getAppReviews(it).await() }
                    val games = response?.asJsonObject
                        ?.get("data")?.asJsonObject
                        ?.get("reviews")?.asJsonArray
                        ?.take(30)
                        ?.map { rank -> rank.asJsonObject.get("appid").toString() }

                } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
            }
            gameTextDetails.setText(gameTextDetailsValue)
        }
}