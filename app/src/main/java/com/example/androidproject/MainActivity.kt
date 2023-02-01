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
import com.example.androidproject.fragments.auth.ForgotPasswordActivity
import com.example.androidproject.fragments.auth.RegisterActivity
import com.example.androidproject.databinding.ActivityMainBinding
import com.example.androidproject.fragments.home.HomeActivity
import com.example.androidproject.utils.createFieldTextWatcher
import com.example.androidproject.utils.fieldResetError
import com.example.androidproject.utils.fieldSetError
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(R.layout.activity_main) {
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

        createFieldTextWatcher(emailTV) { fieldResetError(emailTV) }
        createFieldTextWatcher(passwordTV) { fieldResetError(passwordTV) }
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
            val intent = Intent(this@MainActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkForErrors(email: String, password: String): Boolean {
        var error = false

        if (email.isEmpty()) {
            fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
            error = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
            error = true
        }

        if (password.isEmpty()) {
            fieldSetError(resources, passwordTV, getString(R.string.auth_no_password))
            error = true
        }

        return error
    }

    private fun loginUserAccount() {
        val email = emailTV.text.toString()
        val password = passwordTV.text.toString()

        if (checkForErrors(email, password)) {
            return
        }

        progressBar.visibility = View.VISIBLE

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                progressBar.visibility = View.INVISIBLE

                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, getString(R.string.login_successful), Toast.LENGTH_LONG).show()

                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, getString(R.string.login_failed), Toast.LENGTH_LONG).show()
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