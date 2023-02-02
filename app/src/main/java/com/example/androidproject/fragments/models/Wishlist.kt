package com.example.androidproject.fragments.models

class Wishlist(var userId: String, var whishlist: List<String>) {
    constructor() : this("", listOf())
}