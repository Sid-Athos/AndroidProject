package com.example.androidproject.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.utils.FormsUtils
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

        FormsUtils.createFieldTextWatcherResetError(emailTV)
    }

    private fun initSubmitButtonBehavior() {
        val registerButton: Button = findViewById(R.id.submitButton)
        registerButton.setOnClickListener{ resetPassword() }
    }

    private fun resetPassword() {
        val email = emailTV.text.toString()

        if (email.isEmpty()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTV.text).matches()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.INVISIBLE

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.forgot_password_successful), Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.forgot_password_failed), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        progressBar = findViewById(R.id.progressBar)
    }
}