package com.example.androidproject.services

import retrofit2.http.GET

interface SteamApiService {
    @GET("/ISteamChartsService/GetMostPlayedGames/v1/")
    suspend fun getMostPlayedGames()
}

const val steamApiBaseUrl = "https://api.steampowered.com/"