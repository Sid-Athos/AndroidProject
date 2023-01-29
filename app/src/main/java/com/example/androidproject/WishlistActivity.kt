package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.androidproject.databinding.ActivityWishlistBinding


class WishlistActivity: AppCompatActivity() {
    private lateinit var binding: ActivityWishlistBinding

    private val someInt: Int = 22420

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val myStr = applicationContext.getString(R.string.game_price, 10)
        binding = ActivityWishlistBinding.inflate(layoutInflater)
        initBackButton()
    }

    private fun initBackButton() {
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val homePage = Intent(this, HomeActivity::class.java)
            startActivity(homePage)
        }
    }

    class GameCardAdapter(private val gameIds: List<String>) : RecyclerView.Adapter<GameCardViewHolder>() {
        // Inflate the fragment layout and return a ViewHolder instance
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameCardViewHolder {
            val fragment = GameCardFragment(gameIds[viewType], true)
            return GameCardViewHolder(fragment.requireView())
        }

        // Step 4: Define a method to return an instance of the GameCardFragment class with the string id as a parameter
        private fun getItem(position: Int) = GameCardFragment(gameIds[position], true)

        // Step 5: Find the RecyclerView, set the adapter to an instance of the adapter class and pass the string list as an argument
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home)
            val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
            val adapter = GameCardAdapter(gameIds)
            recyclerView.adapter = adapter
        }

        override fun onBindViewHolder(holder: GameCardViewHolder, position: Int) {
            TODO("Not yet implemented")
        }

        override fun getItemCount(): Int {
            TODO("Not yet implemented")
        }


    }

    class GameCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}