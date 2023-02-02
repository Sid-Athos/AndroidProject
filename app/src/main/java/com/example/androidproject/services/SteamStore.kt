package com.example.androidproject.services

import com.google.gson.JsonElement
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface SteamStoreService {
    @GET("/api/appdetails")
    fun getAppById(@Query("appids") id:String, @Query("l") lang:String = "french") : Deferred<JsonElement>
}