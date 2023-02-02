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
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.MainActivity
import com.example.androidproject.services.SteamApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.example.androidproject.services.SteamStoreService
import com.google.firebase.annotations.concurrent.Background
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import java.net.URL

class GameCardList(private val games: List<String>, private val displayDetails: Boolean, private val parent: Fragment): RecyclerView.Adapter<GameCardList.ViewHolder>() {
    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamStoreService::class.java)

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        private val card: LinearLayout
        private val cover: ImageView
        private val title: TextView
        private val studio: TextView
        private val price: TextView
        private val detailsButton: LinearLayout

        init {
            card = item.findViewById(R.id.game_card)
            cover = item.findViewById(R.id.game_cover)
            title = item.findViewById(R.id.game_title)
            studio = item.findViewById(R.id.game_studio)
            price = item.findViewById(R.id.game_price)
            detailsButton = item.findViewById(R.id.game_details)
        }

        fun bind(data: Game, showDetails: Boolean) {
            bindCoverToBg(data.cover)
            cover.setImageBitmap(data.cover)
            title.text = data.title
            studio.text = data.studio

            if (showDetails) {
                price.visibility = View.VISIBLE
                price.text = itemView.resources.getString(R.string.gamePrice, data.price)

                detailsButton.visibility = View.VISIBLE
            }

            card.visibility = View.VISIBLE
        }

        fun bindCoverToBg(cover: Bitmap) {
            val cardRatio = card.width / card.height
            val banner = Bitmap.createBitmap(cover, 0, cover.height/2, cover.width, cover.width/cardRatio)
            val bannerDrawable = RoundedBitmapDrawableFactory.create(itemView.resources, banner)
            bannerDrawable.cornerRadius = 10F
            bannerDrawable.alpha = 40
            val cardDrawable = ResourcesCompat.getDrawable(itemView.resources, R.drawable.game_card_bg, null)
            val background = LayerDrawable(arrayOf(cardDrawable, bannerDrawable))
            card.background = background
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

        val game = (parent.requireActivity() as MainActivity).cache[gameId]

        if (game == null) {
            GlobalScope.launch(Dispatchers.Main) {
                val data: JsonElement
                val imageBitmap: Bitmap
                withContext(Dispatchers.IO) {
                    data = api.getAppById(gameId).await()
                    imageBitmap = BitmapFactory.decodeStream(coverUrl.openStream())
                }

                Log.v("id gaame list", gameId)

                val newGame = Game(gameId, data, imageBitmap)
                (parent.requireActivity() as MainActivity).cache[gameId] = newGame
                holder.bind(newGame, displayDetails)
            }
        } else holder.bind(game, displayDetails)
    }

    override fun getItemCount(): Int {
        return games.size
    }
}