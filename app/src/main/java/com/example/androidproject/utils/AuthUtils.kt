package com.example.androidproject.utils

import android.content.res.Resources
import com.example.androidproject.R
import com.google.firebase.auth.FirebaseAuthException

class AuthUtils {
    companion object {
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }

        fun isPasswordValid(password: String): Boolean {
            return password.length >= 6
        }

        fun getErrorString(resources: Resources, e: FirebaseAuthException): String {
            return when(e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> resources.getString(R.string.auth_error_user_not_found)
                "ERROR_WRONG_PASSWORD" -> resources.getString(R.string.auth_error_wrong_password)
                "ERROR_USER_DISABLED" -> resources.getString(R.string.auth_error_user_disabled)
                "ERROR_TOO_MANY_REQUESTS" -> resources.getString(R.string.auth_error_too_many_requests)
                "ERROR_EMAIL_ALREADY_IN_USE" -> resources.getString(R.string.auth_error_email_already_in_use)
                else -> resources.getString(R.string.auth_error_unknown)
            }
        }
    }
}