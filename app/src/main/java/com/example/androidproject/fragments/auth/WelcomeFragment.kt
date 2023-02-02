package com.example.androidproject.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.services.AuthService
import com.example.androidproject.services.LikesService
import com.example.androidproject.utils.AuthUtils
import com.example.androidproject.utils.FormsUtils
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*
import java.lang.Exception

class WelcomeFragment : Fragment() {
    private lateinit var emailTV: EditText
    private lateinit var passwordTV: EditText

    private lateinit var progressBar: ProgressBar

    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    private lateinit var authService: AuthService
    private lateinit var forgotPassword: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authService = AuthService()

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
            val action: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToRegisterFragment(
                emailTV.text.toString()
            )
            findNavController().navigate(action)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initLoginButtonBehavior() {
        loginButton.setOnClickListener{
            GlobalScope.launch {
                loginUserAccount()
            }
        }
    }

    private fun initForgotPasswordBehavior() {
        forgotPassword.setOnClickListener {
            val action: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToForgotPasswordFragment(
                emailTV.text.toString()
            )
            findNavController().navigate(action)
        }
    }

    private fun checkForErrors(email: String, password: String): Boolean {
        var error = false

        if (email.isEmpty()) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
            error = true
        } else if (!AuthUtils.isEmailValid(email)) {
            FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
            error = true
        }

        if (password.isEmpty()) {
            FormsUtils.fieldSetError(resources, passwordTV, getString(R.string.auth_no_password))
            error = true
        }

        return error
    }

    private suspend fun loginUserAccount() {
        return withContext(Dispatchers.Main) {
            val email = emailTV.text.toString()
            val password = passwordTV.text.toString()

            if (checkForErrors(email, password)) {
                return@withContext
            }

            progressBar.visibility = View.VISIBLE

            try {
                authService.login(email, password)

                Toast.makeText(context, getString(R.string.login_successful), Toast.LENGTH_LONG).show()

                val action: NavDirections = WelcomeFragmentDirections.actionWelcomeFragmentToHomeFragment()
                findNavController().navigate(action)
            } catch (e: FirebaseAuthException) {
                Log.d("Login", "Error: ${e.errorCode} - ${e.message}")
                Toast.makeText(context, AuthUtils.getAuthErrorString(resources, e), Toast.LENGTH_LONG).show()
            } catch (e: FirebaseTooManyRequestsException) {
                Log.d("Login", "Error: ${e.message}")
                Toast.makeText(context, getString(R.string.auth_error_too_many_requests), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.d("Login", "Error: ${e.message}")
                Toast.makeText(context, getString(R.string.auth_error_unknown), Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.INVISIBLE
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