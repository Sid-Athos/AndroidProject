package com.example.androidproject.fragments.details

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.models.Game
import com.example.androidproject.services.LikesService
import com.example.androidproject.services.SteamStoreService
import com.example.androidproject.services.WishlistService
import com.google.gson.JsonElement
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class GameDetailsFragment: Fragment(R.layout.fragment_game_details) {
    private val likesService = LikesService()
    private val wishlistService = WishlistService()

    private val api = Retrofit.Builder()
        .baseUrl("https://store.steampowered.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(SteamStoreService::class.java)

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val returnButton: AppCompatImageView = requireView().findViewById(R.id.backButton)
        val likeButton: AppCompatImageView = requireView().findViewById(R.id.likesButton)
        val wishlistButton: AppCompatImageView = requireView().findViewById(R.id.wishlistButton)

        val gameId = arguments?.getString("gameId") ?: "10"
        val game = (requireActivity() as MainActivity).cache[gameId]


        likeButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                likesService.add(gameId)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Liked", Toast.LENGTH_SHORT).show()
                }
            }
        }

        wishlistButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                wishlistService.add(gameId)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Added to wishlist", Toast.LENGTH_SHORT).show()
                }
            }
        }

        returnButton.setOnClickListener {
            val action = GameDetailsFragmentDirections.actionGameDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        }


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
        val description: TextView = requireView().findViewById(R.id.description)
        description.text = game.shortDescription

        GlobalScope.launch {
            val backgroundBitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) { game.backgroundUrl.openStream() })
            val coverBitmap = BitmapFactory.decodeStream(withContext(Dispatchers.IO) { game.coverUrl.openStream() })

            withContext(Dispatchers.Main){
                requireView().findViewById<ImageView>(R.id.banner).background = prepareBannerBg(backgroundBitmap)
                requireView().findViewById<ImageView>(R.id.cover).background = BitmapDrawable(resources, coverBitmap)
                val card: LinearLayout = requireView().findViewById(R.id.card)
                card.background = prepareCardBg(card.height, card.width, coverBitmap)
            }
        }

        val showDescription: Button = requireView().findViewById(R.id.showDescription)
        val showReviews: Button = requireView().findViewById(R.id.showReviews)
        showDescription.background = null

        showDescription.setOnClickListener {
            showReviews.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_right_button_bg,null)
            showDescription.background = null
            description.visibility = View.VISIBLE
        }

        showReviews.setOnClickListener {
            showDescription.background = ResourcesCompat.getDrawable(resources, R.drawable.switch_left_button_bg,null)
            showReviews.background = null
            description.visibility = View.GONE
        }
    }

    private fun prepareBannerBg(banner: Bitmap): LayerDrawable {
        val background = BitmapDrawable(resources, banner)
        val shadow = ResourcesCompat.getDrawable(resources, R.drawable.darken_shadow, null)
        return LayerDrawable(arrayOf(background, shadow))
    }

    private fun prepareCardBg(height: Int, width: Int, cover: Bitmap): LayerDrawable {
        val cardRatio = width / height
        val banner = Bitmap.createBitmap(cover, 0, cover.height/2, cover.width, cover.width/cardRatio)
        val bannerDrawable = RoundedBitmapDrawableFactory.create(resources, banner)
        bannerDrawable.cornerRadius = 10F
        bannerDrawable.alpha = 40
        val cardDrawable = ResourcesCompat.getDrawable(resources, R.drawable.game_card_bg, null)
        return LayerDrawable(arrayOf(cardDrawable, bannerDrawable))
    }
}