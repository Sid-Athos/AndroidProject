package com.example.androidproject.services

import com.example.androidproject.fragments.models.Wishlist
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class WishlistService {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private val dbWishlist = db.collection("Wishlist")

    suspend fun add(gameId: String) {
        val wishlist = list()

        if (wishlist.contains(gameId)) {
            return
        }

        val newWishlist = wishlist.toMutableList()
        newWishlist.add(gameId)

        dbWishlist.document(auth.currentUser?.uid!!).set(Wishlist(auth.currentUser?.uid!!, newWishlist)).await()
    }

    suspend fun list(): List<String> {
        val wishlist = dbWishlist.whereEqualTo("userId", auth.currentUser?.uid).get().await().toObjects(Wishlist::class.java)

        if (wishlist.isEmpty()) {
            return listOf()
        }

        return wishlist[0].whishlist
    }

    suspend fun remove(gameId: String) {
        val wishlist = list()

        if (!wishlist.contains(gameId)) {
            return
        }

        val newWishlist = wishlist.toMutableList()
        newWishlist.remove(gameId)

        dbWishlist.document(auth.currentUser?.uid!!).set(Wishlist(auth.currentUser?.uid!!, newWishlist)).await()
    }
}