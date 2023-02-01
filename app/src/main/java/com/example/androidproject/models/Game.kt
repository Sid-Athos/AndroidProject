package com.example.androidproject.models

import com.google.gson.JsonElement

data class Game(val id:String, var title:String, var studio:String, var price:String) {
    constructor(id:String, json: JsonElement):
            this(id,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("name").asString,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("publishers").asJsonArray.get(0).asString,
                json.asJsonObject.get(id).asJsonObject.get("data").asJsonObject.get("price_overview").asJsonObject.get("final_formatted").asString)
}