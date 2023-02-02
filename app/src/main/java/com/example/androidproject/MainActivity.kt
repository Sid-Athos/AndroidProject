package com.example.androidproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.models.Game

class MainActivity : AppCompatActivity() {
    val cache: MutableMap<String, Game> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}