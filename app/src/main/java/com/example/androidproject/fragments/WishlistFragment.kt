package com.example.androidproject.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.R
import com.example.androidproject.services.WishlistService
import kotlinx.coroutines.*

class WishlistFragment : Fragment() {
    private val wishlistService = WishlistService()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        initBackButton(view)

        val recyclerView: RecyclerView = view.findViewById(R.id.wishlist_list_view)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val games = wishlistService.list()
                withContext(Dispatchers.Main) {
                    recyclerView.adapter = GameCardList(games, true, this@WishlistFragment)
                }
            } catch (e: Exception) { Log.e("Game Card Bind:", e.toString()) }
        }

        return view
    }

    private fun initBackButton(view: View) {
        val backButton: ImageView = view.findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val action: NavDirections = WishlistFragmentDirections.actionWishlistFragmentToHomeFragment()
            findNavController().navigate(action)
        }
    }
}