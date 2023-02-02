package com.example.androidproject.fragments.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.androidproject.R
import com.example.androidproject.services.AuthService
import com.example.androidproject.utils.AuthUtils
import com.example.androidproject.utils.FormsUtils
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.*

class ForgotPasswordFragment : Fragment() {
    private lateinit var emailTV: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var submitButton: Button

    private lateinit var authService: AuthService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeUI(view)

        arguments?.let {
            val email = ForgotPasswordFragmentArgs.fromBundle(it).email
            emailTV.setText(email)
        }

        authService = AuthService()

        initSubmitButtonBehavior()

        FormsUtils.createFieldTextWatcherResetError(emailTV)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun initSubmitButtonBehavior() {
        val registerButton: Button = submitButton.findViewById(R.id.submitButton)
        registerButton.setOnClickListener{
            GlobalScope.launch(Dispatchers.Main) {
                resetPassword()
            }
        }
    }

    private suspend fun resetPassword() {
        return withContext(Dispatchers.Main) {
            val email = emailTV.text.toString()

            if (email.isEmpty()) {
                FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_no_email))
                return@withContext
            }

            if (!AuthUtils.isEmailValid(emailTV.text.toString())) {
                FormsUtils.fieldSetError(resources, emailTV, getString(R.string.auth_bad_email))
                return@withContext
            }

            progressBar.visibility = View.VISIBLE

            try {
                authService.resetPassword(email)
                Toast.makeText(context, getString(R.string.forgot_password_successful), Toast.LENGTH_LONG).show()
            } catch (e: FirebaseAuthException) {
                Log.d("ForgotPassword", "Error code: ${e.errorCode}")
                Toast.makeText(context, AuthUtils.getResetPasswordErrorString(resources, e), Toast.LENGTH_LONG).show()
            } catch (e: FirebaseTooManyRequestsException) {
                Log.d("ForgotPassword", "Error: ${e.message}")
                Toast.makeText(context, getString(R.string.auth_error_too_many_requests), Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("ForgotPassword", e.message.toString())
                Toast.makeText(context, getString(R.string.forgot_password_failed), Toast.LENGTH_LONG).show()
            }

            progressBar.visibility = View.INVISIBLE
        }
    }

    private fun initializeUI(view: View) {
        emailTV = view.findViewById(R.id.email)
        progressBar = view.findViewById(R.id.progressBar)

        submitButton = view.findViewById(R.id.submitButton)
    }
}