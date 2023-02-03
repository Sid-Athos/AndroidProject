package com.example.androidproject.models

import android.graphics.Bitmap
import com.google.gson.JsonElement
import java.net.URL

data class Game(val id:String, val title:String, val shortDescription: String, val description: String, val studio:String, val price:String, val coverUrl: URL, val backgroundUrl: URL ) {
    constructor(id:String, json: JsonElement): this(id,
        json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("name").asString,
        json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("short_description").asString,
        json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("detailed_description").asString,
        json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("publishers").asJsonArray.get(0).asString,
        if(json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("is_free").asBoolean) "free"
        else if (!json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.has("price_overview")) "free"
        else json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("price_overview").asJsonObject.get("final_formatted").asString,
        URL("https://steamcdn-a.akamaihd.net/steam/apps/$id/library_600x900.jpg\n"),
        URL(json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("background_raw").asString))
}