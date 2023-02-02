package com.example.androidproject.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.example.androidproject.R
import com.example.androidproject.utils.AuthUtils
import com.example.androidproject.utils.FormsUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


class RegisterFragment : Fragment() {
    private lateinit var usernameTV: EditText
    private lateinit var emailTV: EditText

    private lateinit var passwordTV: EditText
    private lateinit var cpasswordTV: EditText

    private lateinit var regBtn: Button
    private lateinit var progressBar: ProgressBar

    private lateinit var mAuth: FirebaseAuth

    @OptIn(DelicateCoroutinesApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()

        initializeUI(view)

        regBtn.setOnClickListener {
            GlobalScope.launch { registerNewUser() }
        }

        FormsUtils.createFieldTextWatcher(cpasswordTV) { checkPasswordMatch()  }
        FormsUtils.createFieldTextWatcher(passwordTV) {
            checkPasswordLength()
            checkPasswordMatch()
        }

        FormsUtils.createFieldTextWatcherResetError(emailTV)
        FormsUtils.createFieldTextWatcherResetError(usernameTV)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
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
        } else if (!AuthUtils.isEmailValid(emailTV.text.toString())) {
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

    private suspend fun registerNewUser() {
        return withContext(Dispatchers.Main) {
            val email = emailTV.text.toString()
            val password = passwordTV.text.toString()

            if (checkForErrors()) {
                return@withContext
            }

            progressBar.visibility = View.VISIBLE

            try {
                mAuth.createUserWithEmailAndPassword(email, password).await()

                mAuth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(usernameTV.text.toString()).build()
                )?.await()

                Toast.makeText(context, getString(R.string.register_successful), Toast.LENGTH_LONG).show()

                val action: NavDirections = RegisterFragmentDirections.actionRegisterFragmentToWelcomeFragment()
                findNavController().navigate(action)
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun initializeUI(view: View) {
        emailTV = view.findViewById(R.id.email)
        usernameTV = view.findViewById(R.id.username)

        passwordTV = view.findViewById(R.id.password)
        cpasswordTV = view.findViewById(R.id.password_confirm)

        regBtn = view.findViewById(R.id.registerButton)
        progressBar = view.findViewById(R.id.progressBar)
    }
}