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

class ForgotPasswordFragment : Fragment() {
    private lateinit var emailTV: EditText
    private lateinit var progressBar: ProgressBar

    private lateinit var submitButton: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAuth = FirebaseAuth.getInstance()

        initializeUI(view)

        initSubmitButtonBehavior()

        FormsUtils.createFieldTextWatcherResetError(emailTV)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    private fun initSubmitButtonBehavior() {
        val registerButton: Button = submitButton.findViewById(R.id.submitButton)
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
                    Toast.makeText(context, getString(R.string.forgot_password_successful), Toast.LENGTH_LONG).show()

                    val action: NavDirections = ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToWelcomeFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(context, getString(R.string.forgot_password_failed), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI(view: View) {
        emailTV = view.findViewById(R.id.email)
        progressBar = view.findViewById(R.id.progressBar)

        submitButton = view.findViewById(R.id.submitButton)
    }
}