package com.example.androidproject

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
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

        createFieldTextWatcher(cpasswordTV) { checkPasswordMatch()  }
        createFieldTextWatcher(passwordTV) {
            checkPasswordLength()
            checkPasswordMatch()
        }

        createFieldTextWatcher(emailTV) { fieldResetError(emailTV) }
        createFieldTextWatcher(usernameTV) { fieldResetError(usernameTV) }
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

    private fun createFieldTextWatcher(field: EditText, function: () -> Unit) {
        field.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) { return function() }
        })
    }

    private fun checkPasswordLength() {
        if (passwordTV.text.length < 6) {
            fieldSetError(passwordTV, "Le mot de passe doit contenir au moins 6 caractères")
        } else {
            fieldResetError(passwordTV)
        }
    }

    private fun checkPasswordMatch() {
        val passwrd: String = passwordTV.text.toString()
        val cpasswrd: String = cpasswordTV.text.toString()

        if (cpasswrd.isNotEmpty() && passwrd.isNotEmpty()) {
            if (cpasswordTV.text.toString() != passwrd) {
                fieldSetError(cpasswordTV, "Passwords do not match")
            } else {
                fieldResetError(cpasswordTV)
            }
        }
    }

    private fun checkForErrors(): Boolean {
        var error = false

        if (usernameTV.text.isEmpty()) {
            fieldSetError(usernameTV, "Entrez votre nom d'utilisateur")
            error = true
        }

        if (emailTV.text.isEmpty()) {
            fieldSetError(emailTV, getString(R.string.auth_no_email))
            error = true
        } else {
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailTV.text).matches()) {
                fieldSetError(emailTV, "Entrez une adresse email valide")
                error = true
            }
        }

        if (passwordTV.text.isEmpty()) {
            fieldSetError(passwordTV, getString(R.string.auth_no_password))
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
                progressBar.visibility = View.GONE

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