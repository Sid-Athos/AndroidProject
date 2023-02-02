package com.example.androidproject.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.utils.FormsUtils
import com.google.firebase.auth.FirebaseAuth


class WelcomeFragment : Fragment() {
    private lateinit var emailTV: EditText
    private lateinit var passwordTV: EditText

    private lateinit var progressBar: ProgressBar

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var mAuth: FirebaseAuth
    private lateinit var forgotPassword: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        initializeUI(view)

        initRegisterButtonBehavior()
        initLoginButtonBehavior()
        initForgotPasswordBehavior()

        FormsUtils.createFieldTextWatcherResetError(emailTV)
        FormsUtils.createFieldTextWatcherResetError(passwordTV)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    private fun initRegisterButtonBehavior() {
        registerButton.setOnClickListener{
            val action: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment()
            findNavController().navigate(action)
        }
    }

    private fun initLoginButtonBehavior() {
        loginButton.setOnClickListener{ loginUserAccount() }
    }

    private fun initForgotPasswordBehavior() {
        forgotPassword.setOnClickListener {
            val action: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToForgotPasswordFragment()
            findNavController().navigate(action)
        }
    }

    private fun checkForErrors(email: String, password: String): Boolean {
        var error = false

        if (email.isEmpty()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
            error = true
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
            error = true
        }

        if (password.isEmpty()) {
            FormsUtils.fieldSetError(resources, passwordTV, getString(R.string.auth_no_password))
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
                    Toast.makeText(context, getString(R.string.login_successful), Toast.LENGTH_LONG).show()

//                    val intent = Intent(this@MainActivity, HomeActivity::class.java)
//                    startActivity(intent)
                } else {
                    Toast.makeText(context, getString(R.string.login_failed), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI(view: View) {
        emailTV = view.findViewById(R.id.email)
        passwordTV = view.findViewById(R.id.password)

        registerButton = view.findViewById(R.id.registerButton)
        loginButton = view.findViewById(R.id.loginButton)

        progressBar = view.findViewById(R.id.progressBar)
        forgotPassword = view.findViewById(R.id.forgotPassword)
    }
}