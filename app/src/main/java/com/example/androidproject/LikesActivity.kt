package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


class LikesActivity: AppCompatActivity(R.layout.activity_likes) {
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