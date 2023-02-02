package com.example.androidproject.services

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthService {
    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    fun getCurrentUser() = mAuth.currentUser

    fun signOut() = mAuth.signOut()

    fun isUserLoggedIn() = mAuth.currentUser != null

    fun getUid() = mAuth.currentUser?.uid

    suspend fun login(email: String, password: String): AuthResult = mAuth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String) = mAuth.createUserWithEmailAndPassword(email, password).await()
}