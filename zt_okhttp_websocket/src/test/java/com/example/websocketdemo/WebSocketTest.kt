package com.example.websocketdemo

import com.zzt.websocket.WebSocketManager
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class WebSocketTest {
    private lateinit var server: MockWebServer
    private lateinit var manager: WebSocketManager

    @Before
    fun setup() {
        server = MockWebServer()
        server.start()
        manager = WebSocketManager.getInstance()
    }

    @Test
    fun basicConnectionTest() {
        val latch = CountDownLatch(1)
        server.enqueue(MockWebServer.WebSocketReply(
            mode = MockWebServer.WebSocketReply.MODE_SERVER,
            openMessage = "connected"
        ))

        manager.connect(server.url("/ws").toString())
        latch.await(3, TimeUnit.SECONDS)
    }

    @Test
    fun messageDeliveryTest() {
        val testMessage = "test_message"
        val latch = CountDownLatch(1)

        server.enqueue(MockWebServer.WebSocketReply(
            mode = MockWebServer.WebSocketReply.MODE_SERVER,
            onMessage = { _, message ->
                if (message == testMessage) latch.countDown()
            }
        ))

        manager.connect(server.url("/ws").toString())
        manager.sendMessage(testMessage)
        latch.await(5, TimeUnit.SECONDS)
    }

    @Test
    fun reconnectTest() {
        val latch = CountDownLatch(3)
        server.enqueue(MockWebServer.WebSocketReply(
            mode = MockWebServer.WebSocketReply.MODE_FAIL_CONNECTION
        ))

        manager.connect(server.url("/ws").toString())
        latch.await(10, TimeUnit.SECONDS)
    }

    @Test
    fun heartbeatTest() {
        val pingInterval = 3000L
        val latch = CountDownLatch(3)
        
        server.enqueue(MockWebServer.WebSocketReply(
            mode = MockWebServer.WebSocketReply.MODE_SERVER,
            onPing = { _, _ ->
                latch.countDown()
            }
        ))

        manager.apply {
            setPingInterval(pingInterval)
            connect(server.url("/ws").toString())
        }
        assertTrue(latch.await(pingInterval * 4, TimeUnit.MILLISECONDS))
    }

    @Test
    fun reconnectWithBackoffTest() {
        val maxRetries = 3
        val baseInterval = 1000L
        val retryIntervals = mutableListOf<Long>()
        val latch = CountDownLatch(maxRetries)

        repeat(maxRetries) {
            server.enqueue(MockWebServer.WebSocketReply(
                mode = MockWebServer.WebSocketReply.MODE_FAIL_CONNECTION
            ))
        }

        manager.apply {
            setReconnectPolicy(maxRetries, baseInterval)
            setReconnectListener { attempt, delay ->
                retryIntervals.add(delay)
                latch.countDown()
            }
            connect(server.url("/ws").toString())
        }

        latch.await(15, TimeUnit.SECONDS)
        assertEquals(listOf(baseInterval, baseInterval*2, baseInterval*4), retryIntervals)
    }

    @After
    fun teardown() {
        server.shutdown()
        manager.disconnect()
    }
}