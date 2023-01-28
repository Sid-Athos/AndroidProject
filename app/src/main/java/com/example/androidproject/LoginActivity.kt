package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailTV: EditText
    private lateinit var passwordTV: EditText
    private lateinit var loginBtn: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        loginBtn.setOnClickListener { loginUserAccount() }
    }

    private fun loginUserAccount() {
        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.auth_no_email), Toast.LENGTH_LONG).show()
            return
        }
        if (password.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.auth_no_password), Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.login_successful), Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE

                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.login_failed), Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        passwordTV = findViewById(R.id.password)

        loginBtn = findViewById(R.id.login)
        progressBar = findViewById(R.id.progressBar)
    }
}