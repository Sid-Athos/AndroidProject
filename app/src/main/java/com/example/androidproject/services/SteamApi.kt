package com.example.androidproject.services

import com.google.gson.JsonElement
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamApiService {
    @GET("/api/appdetails")
    fun getAppById(@Query("appids") id:String) : Deferred<JsonElement>
}