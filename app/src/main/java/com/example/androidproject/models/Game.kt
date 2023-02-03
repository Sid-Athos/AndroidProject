package com.example.androidproject.models

import android.graphics.Bitmap
import com.google.gson.JsonElement
import java.net.URL

 open class Game(open val id:String, val title:String, val studio:String, val price:String, open val cover: Bitmap, val  description: String ) {
    constructor(id:String, json: JsonElement, cover: Bitmap):
            this(id,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("name").asString,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("publishers").asJsonArray.get(0).asString,
                if(json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("is_free").asBoolean) "free"
                else if (!json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.has("price_overview")) "free"
                else json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("price_overview").asJsonObject.get("final_formatted").asString,
                cover,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("detailed-description").asString)

}