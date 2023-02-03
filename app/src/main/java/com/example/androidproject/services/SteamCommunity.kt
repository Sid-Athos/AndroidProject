package com.example.androidproject.services

import kotlinx.coroutines.Deferred
import com.google.gson.JsonElement
import retrofit2.http.GET
import retrofit2.http.Path

interface SteamCommunityService {
    @GET("/actions/SearchApps/{input}")
    fun searchApps(@Path("input",encoded = true) input:String) : Deferred<JsonElement>
}