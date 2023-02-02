package com.example.androidproject.fragments.gamedetails

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.androidproject.R
import com.example.androidproject.fragments.GameCardList

class GameDetailsFragment : Fragment(R.layout.fragment_game_details) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameTextDetails: TextView = view.findViewById(R.id.game_details_text)
        val gameLogo: ImageView = view.findViewById(R.id.game_details_logo)
        val gameCover: ImageView = view.findViewById(R.id.game_details_cover)

        val games = listOf("2240","970","821","10","2240","970","821","10","2240","970","821","10","2240","970","821","10")
    }
}