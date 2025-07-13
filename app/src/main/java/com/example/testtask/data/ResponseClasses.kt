package com.example.testtask.data

import com.google.gson.annotations.SerializedName

class PingResponse(
    timestamp: Long = System.currentTimeMillis()
) : BaseData(timestamp) {
    @SerializedName("type")
    override val type: String = "ping"
}

class PongResponse(
    timestamp: Long = System.currentTimeMillis()
) : BaseData(timestamp) {
    @SerializedName("type")
    override val type: String = "pong"
} 