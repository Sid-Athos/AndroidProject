package com.example.androidproject.fragments

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapShader
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory

import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.services.SteamApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import java.net.URL

class GameCardList(private val games: List<String>, private val displayDetails: Boolean): RecyclerView.Adapter<GameCardList.ViewHolder>() {
    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamApiService::class.java)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val card: LinearLayout
        private val cover: ImageView
        private val title: TextView
        private val studio: TextView
        private val price: TextView

        init {
            card = item.findViewById(R.id.game_card)
            cover = item.findViewById(R.id.game_cover)
            title = item.findViewById(R.id.game_title)
            studio = item.findViewById(R.id.game_studio)
            price = item.findViewById(R.id.game_price)
        }

        fun bind(data: Game) {
            val cardRatio = card.width / card.height
            val banner = Bitmap.createBitmap(data.cover, 0, data.cover.height/2, data.cover.width, data.cover.width/cardRatio)
            val bannerDrawable = RoundedBitmapDrawableFactory.create(itemView.resources, banner)
            bannerDrawable.cornerRadius = 10F
            bannerDrawable.alpha = 20

            val cardDrawable = ResourcesCompat.getDrawable(itemView.resources, R.drawable.game_card_bg, null)
            val background = LayerDrawable(arrayOf(cardDrawable, bannerDrawable))


            card.background = background
            cover.setImageBitmap(data.cover)
            title.text = data.title
            studio.text = data.studio
            price.text = itemView.resources.getString(R.string.gamePrice, data.price)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.fragment_game_card, viewGroup, false)
        return ViewHolder(view)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gameId = games[position]
        val coverUrl = URL("https://steamcdn-a.akamaihd.net/steam/apps/$gameId/library_600x900.jpg")

        GlobalScope.launch(Dispatchers.Main) {
            try {
                var imageBitmap: Bitmap
                var data: JsonElement

                withContext(Dispatchers.IO) {
                    imageBitmap = BitmapFactory.decodeStream(coverUrl.openStream())
                    data = api.getAppById(gameId).await()
                }

                holder.bind(Game(gameId, data, imageBitmap))
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }
    }

    override fun getItemCount(): Int {
        return games.size
    }
}