package com.example.androidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class GameCardFragment (val id: String, val displayDetails: Boolean) : Fragment(R.layout.fragment_game_card) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_card, container, false)
    }

    companion object {
        fun newInstance() = GameCardFragment()
    }
}