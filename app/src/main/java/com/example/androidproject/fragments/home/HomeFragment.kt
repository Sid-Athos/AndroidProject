package com.example.androidproject.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.fragments.GameCardList
import com.example.androidproject.R

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.game_list_view)
        val games = listOf("2240","970","821","10","2240","970","821","10","2240","970","821","10","2240","970","821","10")
        recyclerView.adapter = GameCardList(games, true)
    }
}