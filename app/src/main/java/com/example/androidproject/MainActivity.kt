package com.example.androidproject

import android.os.Bundle
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivityMainBinding
import com.example.androidproject.fragments.Home
import com.example.androidproject.models.GameResume
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var fragmentManager= supportFragmentManager
        var tx =fragmentManager.beginTransaction()
        val mememe : List<GameResume> =  listOf();
        tx.replace(R.id.container, Home(mememe))
        tx.addToBackStack(null)
        tx.commit()
    }
}