package com.example.androidproject.services

import com.example.androidproject.fragments.models.Likes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LikesService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val dbLikes = db.collection("Likes")

    suspend fun add(gameId: String) {
        val likes = list()

        if (likes.contains(gameId)) {
            return
        }

        val newLikes = likes.toMutableList()
        newLikes.add(gameId)

        dbLikes.document(auth.currentUser?.uid!!).set(Likes(auth.currentUser?.uid!!, newLikes)).await()
    }

    suspend fun list(): List<String> {
        val likes = dbLikes.whereEqualTo("userId", auth.currentUser?.uid).get().await().toObjects(Likes::class.java)

        if (likes.isEmpty()) {
            return listOf()
        }

        return likes[0].likeIds
    }

    suspend fun remove(gameId: String) {
        val likes = list()

        if (!likes.contains(gameId)) {
            return
        }

        val newLikes = likes.toMutableList()
        newLikes.remove(gameId)

        dbLikes.document(auth.currentUser?.uid!!).set(Likes(auth.currentUser?.uid!!, newLikes)).await()
    }
}