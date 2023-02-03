package com.example.androidproject.services

import com.example.androidproject.models.Likes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LikesService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val dbLikes = db.collection("Likes")

    suspend fun add(gameId: String) {
        return withContext(Dispatchers.IO) {
            val likes = list()

            if (likes.contains(gameId)) {
                return@withContext
            }

            val newLikes = likes.toMutableList()
            newLikes.add(gameId)

            dbLikes.document(auth.currentUser?.uid!!).set(Likes(auth.currentUser?.uid!!, newLikes)).await()
        }
    }

    suspend fun list(): List<String> {
        return withContext(Dispatchers.IO) {
            val likes = dbLikes.whereEqualTo("userId", auth.currentUser?.uid).get().await().toObjects(Likes::class.java)

            if (likes.isEmpty()) {
                return@withContext listOf()
            }

            return@withContext likes[0].likeIds
        }
    }

    suspend fun remove(gameId: String) {
        return withContext(Dispatchers.IO) {
            val likes = list()

            if (!likes.contains(gameId)) {
                return@withContext
            }

            val newLikes = likes.toMutableList()
            newLikes.remove(gameId)

            dbLikes.document(auth.currentUser?.uid!!).set(Likes(auth.currentUser?.uid!!, newLikes)).await()
        }
    }
}