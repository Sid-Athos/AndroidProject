package com.example.androidproject.services

import com.google.gson.JsonElement
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface SteamApiService {
    @GET("/ISteamChartsService/GetMostPlayedGames/v1/?")
    fun getMostSelledGames() : Deferred<JsonElement>
}