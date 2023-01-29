package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var emailTV: EditText
    private lateinit var passwordTV: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    private lateinit var forgotPassword: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        initRegisterButtonBehavior()
        initLoginButtonBehavior()
        initForgotPasswordBehavior()
    }

    private fun initRegisterButtonBehavior() {
        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener{
            val registerActivity = Intent(this, RegisterActivity::class.java)
            startActivity(registerActivity)
        }
    }

    private fun initLoginButtonBehavior() {
        val registerButton: Button = findViewById(R.id.loginButton)
        registerButton.setOnClickListener{ loginUserAccount() }
    }

    private fun initForgotPasswordBehavior() {
        forgotPassword.setOnClickListener {
            println("Forgot password")

            // val intent = Intent(this@MainActivity, ForgotPasswordActivity::class.java)
            // startActivity(intent)
        }
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

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
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

        progressBar = findViewById(R.id.progressBar)
        forgotPassword = findViewById(R.id.forgotPassword)
    }
}