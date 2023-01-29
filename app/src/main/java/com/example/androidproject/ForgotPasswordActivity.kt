package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

        emailTV.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                fieldResetError(emailTV)
            }
        })

        initSubmitButtonBehavior()
    }

    private fun fieldSetError(field: EditText, error: String) {
        val icon = ResourcesCompat.getDrawable(resources, R.drawable.warning, null)

        icon?.setBounds(
            0, 0,
            icon.intrinsicWidth,
            icon.intrinsicHeight
        )

        field.setError(error, icon)

        field.setBackgroundResource(R.drawable.auth_field_bg_error)
    }

    private fun fieldResetError(field: EditText) {
        field.error = null
        field.setBackgroundResource(R.drawable.auth_field_bg)
    }

    private fun initSubmitButtonBehavior() {
        val registerButton: Button = findViewById(R.id.submitButton)
        registerButton.setOnClickListener{ resetPassword() }
    }

    private fun resetPassword() {
        val email = emailTV.text.toString()

        if (email.isEmpty()) {
            fieldSetError(emailTV, getString(R.string.auth_no_email))
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTV.text).matches()) {
            fieldSetError(emailTV, getString(R.string.auth_bad_email))
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