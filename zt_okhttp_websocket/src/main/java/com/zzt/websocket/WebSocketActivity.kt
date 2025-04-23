package com.zzt.websocket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.zzt.zt_okhttp_websocket.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.ByteString


/**
 * @author: zeting
 * @date: 2025/4/23
 */
class WebSocketActivity : AppCompatActivity() {
    private var server: MockWebServer? = null
    private var sWebSocket: WebSocket? = null
    var manager: WebSocketManager? = null

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, WebSocketActivity::class.java)
            context.startActivity(starter)
        }

        val TAG = "WebSocket"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var button0: Button = findViewById(R.id.button0)
        var button1: Button = findViewById(R.id.button1)
        var button2: Button = findViewById(R.id.button2)
        var button3: Button = findViewById(R.id.button3)

        button0.setOnClickListener {
            startService()
        }

        button1.setOnClickListener {
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    manager = WebSocketManager.getInstance()
                    var wsUrl = server?.url("/").toString()
                    manager?.connect(wsUrl)
                }
            }

        }

        button2.setOnClickListener {
            manager?.sendMessage("发送消息给服务器")

        }

        button3.setOnClickListener {
            manager?.disconnect()

        }


    }

    fun startService() {
        lifecycleScope.launch(context = Dispatchers.IO) {
            server = MockWebServer()
            val response =
                MockResponse().withWebSocketUpgrade(object : WebSocketListener() {
                    override fun onOpen(webSocket: WebSocket, response: Response) {
                        super.onOpen(webSocket, response)
                        //有客户端连接时回调
                        Log.e(TAG, "服务器收到客户端连接成功")
                        sWebSocket = webSocket
                        sWebSocket?.send("> 你已经连接上服务器了")
                    }

                    override fun onMessage(webSocket: WebSocket, text: String) {
                        super.onMessage(webSocket, text)
                        Log.e(TAG, "服务器收到消息：$text")

                        if ("ping" == text) {
                            sWebSocket?.send("> ping")
                        } else {
                            sWebSocket?.send("> 我已经收到你的消息，给你个回执")
                        }
                    }

                    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                        super.onMessage(webSocket, bytes)
                        Log.e(TAG, "服务器收到消息 bytes ：$bytes")
                    }

                    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                        super.onClosed(webSocket, code, reason)
                        Log.e(TAG, "客户端退出");
                        sWebSocket = null;
                    }

                    override fun onFailure(
                        webSocket: WebSocket, t: Throwable, @Nullable response: Response?
                    ) {
                        super.onFailure(webSocket, t, response)
                        Log.e(TAG, "连接失败!")
                        sWebSocket = null
                    }

                    override fun onClosing(
                        webSocket: WebSocket,
                        code: Int,
                        reason: String
                    ) {
                        super.onClosing(webSocket, code, reason)
                        Log.e(TAG, "正在关闭!")
                        sWebSocket?.close(1000, "正常关闭")
                        sWebSocket = null
                    }
                })
            server?.enqueue(response)
            server?.start()
            // 获取服务器地址
            val serverUrl = server?.url("/").toString()
            Log.d(TAG, "服务器已启动，地址: $serverUrl")
        }
    }
}
