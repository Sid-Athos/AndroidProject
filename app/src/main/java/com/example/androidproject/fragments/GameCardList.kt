package com.example.androidproject.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.LayerDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.MainActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.example.androidproject.services.SteamStoreService
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*

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

        @OptIn(DelicateCoroutinesApi::class)
        fun bind(data: Game, showDetails: Boolean) {
            GlobalScope.launch {
                Log.v("game list cover", data.coverUrl.toString())
                val coverBitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                    data.coverUrl.openStream()
                })
                withContext(Dispatchers.Main){
                    bindCoverToBg(coverBitmap)
                    cover.setImageBitmap(coverBitmap)
                }
            }
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
        val game = (parent.requireActivity() as MainActivity).cache[gameId]

        if (game == null) {
            GlobalScope.launch(Dispatchers.Main) {
                val data: JsonElement
                withContext(Dispatchers.IO) {
                    data = api.getAppById(gameId).await()
                }

                if (data.asJsonObject.get(gameId).asJsonObject.get("success").asBoolean) {
                    val newGame = Game(gameId, data)
                    (parent.requireActivity() as MainActivity).cache[gameId] = newGame
                    holder.bind(newGame, displayDetails)
                }
            }
        } else holder.bind(game, displayDetails)
    }

    override fun getItemCount(): Int {
        return games.size
    }
}