package com.example.androidproject.models

import android.graphics.Bitmap
import com.google.gson.JsonElement

data class Game(val id:String, val title:String, val studio:String, val price:String, val cover: Bitmap ) {
    constructor(id:String, json: JsonElement, cover: Bitmap):
            this(id,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("name").asString,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("publishers").asJsonArray.get(0).asString,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("price_overview").asJsonObject.get("final_formatted").asString,
                cover)
}