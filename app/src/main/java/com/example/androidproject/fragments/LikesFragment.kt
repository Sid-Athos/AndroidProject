package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.services.LikesService
import kotlinx.coroutines.*


class LikesFragment: Fragment() {
    private val likesService = LikesService()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_likes, container, false)

        initBackButton(view)

        val recyclerView: RecyclerView = view.findViewById(R.id.likes_list_view)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val games = likesService.list()
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = GameCardList(games, true, this@LikesFragment)
                }
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }

        return view
    }

    private fun initBackButton(view: View) {
        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val action: NavDirections = LikesFragmentDirections.actionLikesFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}