package com.example.testtask

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import com.example.testtask.data.WebSocketManager
import com.example.testtask.data.PingResponse
import com.example.testtask.data.PongResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : BaseObservable() {
    private val connectionManager = WebSocketManager("88.201.236.21", 4445)

    val connectionState = MutableLiveData<String>()

    private val messageStrings = mutableListOf<String>()

    init {
        CoroutineScope(Dispatchers.Main).launch {
            connectionManager.responses.collect { response ->
                val message = when (response) {
                    is PingResponse -> "${response.type} at ${response.ts}"
                    is PongResponse -> "${response.type} at ${response.ts}"
                    else -> "unknown message"
                }
                messageStrings.add(message)
            }
        }

        CoroutineScope(Dispatchers.Main).launch {
            connectionManager.connectionState.collect { isConnected ->
                connectionState.value = "Status: ${if (isConnected) "Connected" else "Disconnected"}"
            }
        }
    }

    fun connectToWebSocket() {
        connectionManager.connect()
    }

    fun disconnectFromWebSocket() {
        connectionManager.disconnect()
    }

    fun sendPing() {}

    fun sendPong() {}

    fun prettifyPong() {}
} 