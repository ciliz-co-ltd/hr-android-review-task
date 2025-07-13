package com.example.testtask.data

import com.google.gson.annotations.SerializedName

abstract class BaseData(
    @SerializedName("ts")
    val ts: Long = System.currentTimeMillis()
) {
    abstract val type: String
} 