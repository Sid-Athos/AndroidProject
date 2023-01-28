package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initLoginButtonBehavior()
        initRegisterButtonBehavior()
    }

    private fun initLoginButtonBehavior() {
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener{
            val loginActivity = Intent(this, LoginActivity::class.java)
            startActivity(loginActivity)
        }
    }

    private fun initRegisterButtonBehavior() {
        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener{
            val registerActivity = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivity)
        }
    }
}