package com.example.testtask.data

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import okhttp3.*

class WebSocketManager(
    private val host: String,
    private val port: Int
) {

    val responses = MutableSharedFlow<BaseData>()
    val connectionState = MutableStateFlow(false)

    fun connect() {}

    fun disconnect() {}
} 