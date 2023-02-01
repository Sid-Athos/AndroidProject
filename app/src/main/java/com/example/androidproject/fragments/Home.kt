package com.example.androidproject.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.androidproject.R
import com.example.androidproject.models.GameResume

class Home(news: List<GameResume>) : Fragment() {

    private  val gamesToDisplay = news


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activity_home, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gamesToDisplay.forEach {
            println("$it")
        }
        initLikeButtonBehavior()
        initWishlistButtonBehavior()
    }

    private fun initLikeButtonBehavior() {
        val likesButton: ImageView = requireView().findViewById(R.id.likesButton)
    }

    private fun initWishlistButtonBehavior() {
        val wishlistButton: ImageView = requireView().findViewById(R.id.wishlistButton)
    }
}