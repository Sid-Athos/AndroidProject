package com.example.androidproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById(R.id.game_list_view)
        val color = resources.getColor(R.color.white)
        recyclerView.setBackgroundColor(color)
        val games = listOf("2240","970","821","10","2240","970","821","10","2240","970","821","10","2240","970","821","10")
        recyclerView.adapter = GameCardList(games, true)
    }
}