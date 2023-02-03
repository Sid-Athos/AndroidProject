package com.example.androidproject.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthService {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    init {
        mAuth.useAppLanguage()
    }

    fun getCurrentUser() = mAuth.currentUser

    fun signOut() = mAuth.signOut()

    fun isUserLoggedIn() = mAuth.currentUser != null

    fun getUid() = mAuth.currentUser?.uid

    suspend fun login(email: String, password: String) {
        return withContext(Dispatchers.IO) {
            mAuth.signInWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun register(email: String, password: String, username: String): AuthResult? {
        return withContext(Dispatchers.IO) {
            val res = mAuth.createUserWithEmailAndPassword(email, password).await()
            mAuth.currentUser?.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName(username)
                    .build()
            )?.await()

            return@withContext res
        }
    }

    suspend fun resetPassword(email: String): Void? {
        return withContext(Dispatchers.IO) {
            mAuth.sendPasswordResetEmail(email).await()
        }
    }
}