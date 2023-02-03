package com.example.androidproject.fragments.gamedetails

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.androidproject.R

class GameDetailsFragment : Fragment(R.layout.fragment_game_details) {

    companion object{
        fun newInstance(game: GameDetails) : GameDetailsFragment {
            val fragment = GameDetailsFragment()
            val args = bundleOf("key" to game)
            fragment.arguments = args
            val gameTextDetails: TextView = fragment.requireView().findViewById(R.id.game_details_text)
            val gameLogo: ImageView = fragment.requireView().findViewById(R.id.game_details_logo)
            val gameCover: ImageView = fragment.requireView().findViewById(R.id.game_details_cover)

            gameTextDetails.setText(game.)
            return fragment
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val games = listOf("2240","970","821","10","2240","970","821","10","2240","970","821","10","2240","970","821","10")
    }
}