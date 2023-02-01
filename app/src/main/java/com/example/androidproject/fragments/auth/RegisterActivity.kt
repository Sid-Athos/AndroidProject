package com.example.androidproject.fragments.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.example.androidproject.utils.FormsUtils
import com.google.firebase.auth.FirebaseAuth


class RegisterActivity : AppCompatActivity() {
    private lateinit var usernameTV: EditText
    private lateinit var emailTV: EditText

    private lateinit var passwordTV: EditText
    private lateinit var cpasswordTV: EditText

    private lateinit var regBtn: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()

        initializeUI()

        regBtn.setOnClickListener { registerNewUser() }

        FormsUtils.createFieldTextWatcher(cpasswordTV) { checkPasswordMatch()  }
        FormsUtils.createFieldTextWatcher(passwordTV) {
            checkPasswordLength()
            checkPasswordMatch()
        }

        FormsUtils.createFieldTextWatcherResetError(emailTV)
        FormsUtils.createFieldTextWatcherResetError(usernameTV)
    }

    private fun checkPasswordLength() {
        if (passwordTV.text.length < 6) {
            FormsUtils.fieldSetError(resources, passwordTV, getString(R.string.auth_bad_password))
        } else {
            FormsUtils.fieldResetError(passwordTV)
        }
    }

    private fun checkPasswordMatch() {
        val passwrd: String = passwordTV.text.toString()
        val cpasswrd: String = cpasswordTV.text.toString()

        if (cpasswrd.isNotEmpty() && passwrd.isNotEmpty()) {
            if (cpasswordTV.text.toString() != passwrd) {
                FormsUtils.fieldSetError(resources, cpasswordTV, getString(R.string.auth_passwords_not_matching))
            } else {
                FormsUtils.fieldResetError(cpasswordTV)
            }
        }
    }

    private fun checkForErrors(): Boolean {
        var error = false

        if (usernameTV.text.isEmpty()) {
            FormsUtils.fieldSetError(resources, usernameTV, getString(R.string.auth_no_username))
            error = true
        }

        if (emailTV.text.isEmpty()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
            error = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTV.text).matches()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
            error = true
        }

        if (passwordTV.text.isEmpty()) {
            FormsUtils.fieldSetError(resources, passwordTV, getString(R.string.auth_no_password))
            error = true
        }

        if (passwordTV.error != null) {
            error = true
        }

        if (cpasswordTV.error != null) {
            error = true
        }

        return error
    }

    private fun registerNewUser() {
        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()

        if (checkForErrors()) {
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.INVISIBLE

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.register_successful), Toast.LENGTH_LONG).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.register_failed), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = findViewById(R.id.email)
        usernameTV = findViewById(R.id.username)

        passwordTV = findViewById(R.id.password)
        cpasswordTV = findViewById(R.id.password_confirm)

        regBtn = findViewById(R.id.registerButton)
        progressBar = findViewById(R.id.progressBar)
    }
}