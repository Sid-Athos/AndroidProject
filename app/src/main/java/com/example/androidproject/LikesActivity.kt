package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivityLikesBinding


class LikesActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLikesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLikesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBackButton()
    }

    private fun initBackButton() {
        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val homePage = Intent(this, MainActivity::class.java)
            startActivity(homePage)
        }
    }
}