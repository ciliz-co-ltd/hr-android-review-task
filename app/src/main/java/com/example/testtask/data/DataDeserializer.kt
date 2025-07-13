package com.example.testtask.data

import com.google.gson.*
import java.lang.reflect.Type

class DataDeserializer : JsonDeserializer<BaseData> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): BaseData {
        val jsonObject = json.asJsonObject
        return when (jsonObject.get("type").asString) {
            else -> throw JsonParseException("Unknown message type: $jsonObject")
        }
    }
}