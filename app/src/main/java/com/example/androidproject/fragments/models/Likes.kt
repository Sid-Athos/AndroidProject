package com.example.androidproject.fragments.models

class Likes(var userId: String, var likeIds: List<String>) {
    constructor() : this("", listOf())
}