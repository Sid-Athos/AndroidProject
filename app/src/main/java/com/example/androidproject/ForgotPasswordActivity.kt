package com.example.androidproject

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var emailTV: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forgot_password)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        initSubmitButtonBehavior()
    }

    private fun initSubmitButtonBehavior() {
        val registerButton: Button = findViewById(R.id.submitButton)
        registerButton.setOnClickListener{ resetPassword() }
    }

    private fun resetPassword() {
        val email = emailTV.text.toString()

        if (email.isEmpty()) {
            Toast.makeText(applicationContext, getString(R.string.auth_no_email), Toast.LENGTH_LONG).show()
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.GONE

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Code sent", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(applicationContext, "Code send failed", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        progressBar = findViewById(R.id.progressBar)
    }
}