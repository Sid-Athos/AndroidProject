package com.example.androidproject.models

class Likes(var userId: String, var likeIds: List<String>) {
    constructor() : this("", listOf())
}