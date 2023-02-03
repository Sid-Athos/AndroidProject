package com.example.androidproject.services

import com.google.gson.JsonElement
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ReviewsService {
    @GET("/appreviews/appdetails")
    fun getAppReviews(@Query("appid") id:String, @Query("l") lang:String = "french") : Deferred<JsonElement>
}