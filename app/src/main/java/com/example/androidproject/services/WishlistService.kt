package com.example.androidproject.services

import com.example.androidproject.models.Wishlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class WishlistService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val dbWishlist = db.collection("Wishlist")

    suspend fun add(gameId: String) {
        return withContext(Dispatchers.IO) {
            val wishlist = list()

            if (wishlist.contains(gameId)) {
                return@withContext
            }

            val newWishlist = wishlist.toMutableList()
            newWishlist.add(gameId)

            dbWishlist.document(auth.currentUser?.uid!!).set(Wishlist(auth.currentUser?.uid!!, newWishlist)).await()
        }
    }

    suspend fun list(): List<String> {
        return withContext(Dispatchers.IO) {
            val wishlist = dbWishlist.whereEqualTo("userId", auth.currentUser?.uid).get().await().toObjects(Wishlist::class.java)

            if (wishlist.isEmpty()) {
                return@withContext listOf()
            }

            return@withContext wishlist[0].whishlist
        }
    }

    suspend fun remove(gameId: String) {
        return withContext(Dispatchers.IO) {
            val wishlist = list()

            if (!wishlist.contains(gameId)) {
                return@withContext
            }

            val newWishlist = wishlist.toMutableList()
            newWishlist.remove(gameId)

            dbWishlist.document(auth.currentUser?.uid!!).set(Wishlist(auth.currentUser?.uid!!, newWishlist)).await()
        }
    }
}