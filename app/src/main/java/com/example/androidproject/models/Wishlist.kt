package com.example.androidproject.models

class Wishlist(var userId: String, var whishlist: List<String>) {
    constructor() : this("", listOf())
}