package com.example.androidproject.fragments.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.example.androidproject.services.SteamStoreService
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MostPlayedGame: Fragment(R.layout.fragment_most_played_game) {
    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamStoreService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    fun bind(gameId: String) {
        val game = (requireParentFragment().requireActivity() as MainActivity).cache[gameId]

        if (game == null) {
            GlobalScope.launch {
                val data: JsonElement
                withContext(Dispatchers.IO) { data = api.getAppById(gameId).await() }

                if (data.asJsonObject.get(gameId).asJsonObject.get("success").asBoolean) {
                    val newGame = Game(gameId, data)
                    (requireParentFragment().requireActivity() as MainActivity).cache[gameId] = newGame
                    withContext(Dispatchers.Main) { render(newGame) }
                }
            }
        } else render(game)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun render(game: Game) {
        requireView().findViewById<TextView>(R.id.title).text = game.title
        requireView().findViewById<TextView>(R.id.description).text = game.shortDescription

        GlobalScope.launch {
            val backgroundBitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) { game.backgroundUrl.openStream() })
            val coverBitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) { game.coverUrl.openStream() })
            val container: LinearLayout = requireView().findViewById(R.id.container)

            withContext(Dispatchers.Main){
                requireView().findViewById<ImageView>(R.id.cover).background = BitmapDrawable(resources, coverBitmap)
                container.background = prepareBg(backgroundBitmap)
                container.visibility = View.VISIBLE
            }
        }

    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun prepareBg(bitmap: Bitmap): LayerDrawable {
        val background = BitmapDrawable(resources, bitmap)
        val shadow = ResourcesCompat.getDrawable(resources, R.drawable.darken_shadow, null)
        return LayerDrawable(arrayOf(background, shadow))
    }
}