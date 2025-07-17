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
    private var ws: WebSocket? = null
    private var scope: CoroutineScope? = null
    private val c = OkHttpClient.Builder().build()

    private val gson: Gson

    val responses = MutableSharedFlow<BaseData>()
    val connectionState = MutableStateFlow(false)

    init {
        val dataDeserializer = DataDeserializer()

        gson = GsonBuilder()
            .registerTypeAdapter(BaseData::class.java, dataDeserializer)
            .create()
    }

    suspend fun buildMessage(bytes: okio.ByteString): BaseData {
        return withContext(Dispatchers.IO) {
            gson.fromJson(bytes.utf8(), BaseData::class.java)
        }
    }

    fun connect() {
        scope?.cancel()
        scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        scope?.launch {
            try {
                val request = Request.Builder()
                    .url("ws://$host:$port/websocket")
                    .build()

                ws = c.newWebSocket(request, object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        scope?.launch {
                            connectionState.emit(true)
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: okio.ByteString) {
                        scope?.launch {
                            val message = buildMessage(bytes)
                            responses.emit(message)
                        }
                    }

                    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                        scope?.launch {
                            connectionState.emit(false)
                        }
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        scope?.launch {
                            connectionState.emit(false)
                        }
                    }

                    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                        scope?.launch {
                            connectionState.emit(false)
                        }
                    }
                })
            } catch (_: Exception) { }
        }
    }

    suspend fun send(request: BaseData) {
        withContext(Dispatchers.IO) {
            val jsonString = gson.toJson(request)
            ws?.send(jsonString) ?: false
        }
    }

    fun disconnect() {
        ws?.close(1000, "Client disconnecting")
        ws = null
        scope?.cancel()
        scope?.launch {
            connectionState.emit(false)
        }
        scope = null
    }
} 