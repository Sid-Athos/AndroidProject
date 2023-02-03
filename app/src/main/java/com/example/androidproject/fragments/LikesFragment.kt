package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.services.LikesService
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.*


class LikesFragment: Fragment() {
    private val likesService = LikesService()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_likes, container, false)

        initBackButton(view)

        val recyclerView: RecyclerView = view.findViewById(R.id.likes_list_view)
        val progressBar = view.findViewById<ProgressBar>(R.id.likes_progress_bar)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val games = likesService.list()
                withContext(Dispatchers.Main) {
<<<<<<< HEAD
                    if (games.isEmpty()) {
                        view.findViewById<MaterialTextView>(R.id.empty_likes).visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    } else {
                        recyclerView.adapter = GameCardList(games, true, this@LikesFragment)
                        recyclerView.scheduleLayoutAnimation()
                    }

                    progressBar.visibility = View.GONE
=======
                    recyclerView.adapter = GameCardList(games, R.id.go_to_details, this@LikesFragment)
>>>>>>> 1a435db283ea0829f835bed2f781ab7b16062f23
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