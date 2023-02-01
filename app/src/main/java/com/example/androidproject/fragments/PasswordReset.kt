package com.example.androidproject.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.androidproject.MainActivity
import com.example.androidproject.R
import com.google.firebase.auth.FirebaseAuth

class PasswordReset : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var emailTV: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        val registerButton: Button = requireView().findViewById(R.id.submitButton)
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
                    Toast.makeText(activity, getString(R.string.forgot_password_successful), Toast.LENGTH_LONG).show()
                    /**
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    */
                } else {
                    Toast.makeText(activity, getString(R.string.forgot_password_failed), Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun initializeUI() {
        emailTV = requireView().findViewById(R.id.email)
        progressBar = requireView().findViewById(R.id.progressBar)
    }
}