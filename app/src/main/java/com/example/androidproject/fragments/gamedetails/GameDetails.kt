package com.example.androidproject.fragments.gamedetails

import android.graphics.Bitmap
import com.example.androidproject.models.Game
import com.google.gson.JsonElement

open class GameDetails(override val id: String, val json: JsonElement, override val cover: Bitmap, val details : String) : Game(id,  cover) {
   constructor( json: JsonElement) : this(id,
       json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("name").asString,
       json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("publishers").asJsonArray.get(0).asString,
       if(json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("is_free").asBoolean) "free"
       else if (!json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.has("price_overview")) "free"
       else json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("price_overview").asJsonObject.get("final_formatted").asString,
       cover)
