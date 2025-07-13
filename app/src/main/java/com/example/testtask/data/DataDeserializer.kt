package com.example.testtask.data

import com.google.gson.*
import java.lang.reflect.Type

class DataDeserializer : JsonDeserializer<BaseData> {

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(BaseData::class.java, this)
        .create()
    
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BaseData {
        val jsonObject = json.asJsonObject
        return when (jsonObject.get("type").asString) {
            "ping" -> gson.fromJson(json, PingResponse::class.java)
            "pong" -> gson.fromJson(json, PongResponse::class.java)
            else -> throw JsonParseException("Unknown message type: $jsonObject")
        }
    }
}