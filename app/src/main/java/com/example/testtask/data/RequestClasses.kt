package com.example.testtask.data

import com.google.gson.annotations.SerializedName

class PingRequest(
    timestamp: Long = System.currentTimeMillis()
) : BaseData(timestamp) {
    @SerializedName("type")
    override val type: String = "ping"
}

class PongRequest(
    timestamp: Long = System.currentTimeMillis()
) : BaseData(timestamp) {
    @SerializedName("type")
    override val type: String = "pong"
} 