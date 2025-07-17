package com.example.testtask

import androidx.databinding.BaseObservable
import androidx.lifecycle.MutableLiveData
import com.example.testtask.data.WebSocketManager
import com.example.testtask.Utils.Companion.utcMsToSeconds
import com.example.testtask.data.PingRequest
import com.example.testtask.data.PingResponse
import com.example.testtask.data.PongRequest
import com.example.testtask.data.PongResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : BaseObservable() {
    private val connectionManager = WebSocketManager("88.201.236.21", 4445)

    val messageHistory = MutableLiveData<List<String>>()
    val connectionState = MutableLiveData<String>()

    data class Message(var type: String, var ts: Long)

    private val history = mutableListOf<Message>()

    private var filter: Boolean = true

    private val prettyPongs get() = history.filter { it.type == "pongs" }

    private val prettyAll get() = history.map { "${it.type} at ${it.ts}" }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            connectionManager.responses.collect { response ->
                when (response) {
                    is PingResponse,
                    is PongResponse -> {
                        history.add(Message(response.type, response.timestamp))
                        messageHistory.value = prettyAll
                    }
                    else -> "unknown message"
                }
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

    fun sendPing() {
        CoroutineScope(Dispatchers.IO).launch {
            val pingRequest = PingRequest()
            connectionManager.send(pingRequest)
        }
    }

    fun sendPong() {
        CoroutineScope(Dispatchers.IO).launch {
            val pongRequest = PongRequest()
            connectionManager.send(pongRequest)
        }
    }

    fun prettifyPong() {
        if (filter) {
            val temp = history.toMutableList()
            temp.forEach {
                it.ts = utcMsToSeconds(it.ts)
                if (it.type == "ping") {
                    temp.remove(it)
                }
            }

            messageHistory.value = temp.map { "${it.type} at ${it.ts}" }
        } else {
            messageHistory.value = prettyAll
        }
        filter = !filter
    }
} 