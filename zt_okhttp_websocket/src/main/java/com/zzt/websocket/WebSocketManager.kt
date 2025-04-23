package com.zzt.websocket

import android.util.Log
import okhttp3.*
import okio.ByteString
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import kotlinx.coroutines.*
import okio.ByteString.Companion.encodeUtf8
import kotlin.math.log

class WebSocketManager private constructor() {
    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    private var webSocket: WebSocket? = null
    private var retryCount = 0 // 重连次数
    private var HEART_TIME = 20 * 1000L // 心跳间隔
    private val isConnected = AtomicBoolean(false)
    private var heartbeatJob: Job? = null
    val TAG = "WebSocketManager"

    companion object {
        @Volatile
        private var instance: WebSocketManager? = null

        fun getInstance(): WebSocketManager {
            return instance ?: synchronized(this) {
                instance ?: WebSocketManager().also { instance = it }
            }
        }
    }

    fun connect(url: String?) {
        if (url.isNullOrBlank()) return
        if (isConnected.get()) return

        val request = Request.Builder()
            .url(url)
            .build()
        Log.w(TAG, "client connect url:" + url)

        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.w(TAG, "客户端 onOpen 我链接上服务端了，开启心跳")

                isConnected.set(true)
                retryCount = 0
                startHeartbeat()
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.w(TAG, "客户端 onMessage text:" + text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                Log.w(TAG, "客户端 onClosed code:" + code + " reason:" + reason)

                isConnected.set(false)
                stopHeartbeat()
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.w(TAG, "客户端 onFailure t:" + t + " response:" + response)

                isConnected.set(false)
                stopHeartbeat()
                scheduleReconnect()

                t.printStackTrace()
            }
        })
    }

    private fun startHeartbeat() {
        heartbeatJob = CoroutineScope(Dispatchers.IO).launch {
            while (isConnected.get()) {
                Log.i(TAG, "客户端 发送心跳。。。。。。")

                webSocket?.send("ping")
                delay(HEART_TIME)
            }
        }
    }

    private fun stopHeartbeat() {
        heartbeatJob?.cancel()
    }

    fun sendMessage(message: String) {
        if (isConnected.get()) {
            webSocket?.send(message)
        }
    }

    private fun scheduleReconnect() {
        retryCount++
        CoroutineScope(Dispatchers.IO).launch {
            delay(Math.min(20 * 1000L, HEART_TIME * retryCount))
            connect(webSocket?.request()?.url.toString())
        }
    }

    fun disconnect() {
        webSocket?.close(1000, "Manual disconnect")
        isConnected.set(false)
        stopHeartbeat()
    }
}