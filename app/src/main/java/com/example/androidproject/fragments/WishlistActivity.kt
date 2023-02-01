package com.example.androidproject.fragments

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.R
import com.example.androidproject.fragments.home.HomeActivity


class WishlistActivity: AppCompatActivity(R.layout.activity_wishlist) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBackButton()
    }

    private fun initBackButton() {
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val homePage = Intent(this, HomeActivity::class.java)
            startActivity(homePage)
        }
    }
}