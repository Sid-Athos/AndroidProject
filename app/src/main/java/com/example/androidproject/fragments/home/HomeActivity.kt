package com.example.androidproject.fragments.home

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.fragments.LikesFragment
import com.example.androidproject.R
import com.example.androidproject.fragments.WishlistFragment
import com.example.androidproject.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLikeButtonBehavior()
        initWishlistButtonBehavior()
    }

    private fun initLikeButtonBehavior() {
        val likesButton: ImageView = findViewById(R.id.likesButton)
        likesButton.setOnClickListener{
            val likesFragment = Intent(this, LikesFragment::class.java)
            startActivity(likesFragment)
        }
    }

    private fun initWishlistButtonBehavior() {
        val wishlistButton: ImageView = findViewById(R.id.wishlistButton)
        wishlistButton.setOnClickListener{
            val wishlistFragment = Intent(this, WishlistFragment::class.java)
            startActivity(wishlistFragment)
        }
    }
}