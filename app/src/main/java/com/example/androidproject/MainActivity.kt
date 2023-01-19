package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivityHomeBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLikeButtonBehavior();
        initWishlistButtonBehavior();
    }

    private fun initLikeButtonBehavior() {
        val likesButton: ImageView = findViewById(R.id.likesButton)
        likesButton.setOnClickListener{
            val likesActivity = Intent(this, LikesActivity::class.java)
            startActivity(likesActivity)
        }
    }

    private fun initWishlistButtonBehavior() {
        val wishlistButton: ImageView = findViewById(R.id.wishlistButton)
        wishlistButton.setOnClickListener{
            val wishlistActivity = Intent(this, WishlistActivity::class.java)
            startActivity(wishlistActivity)
        }
    }
}