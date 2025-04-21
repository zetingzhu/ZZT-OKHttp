package com.zzt.zt_newwork_test
import okhttp3.*
import java.io.IOException

/**
 * @author: zeting
 * @date: 2025/4/21
 *
 */
class TimeLoggingEventListener : EventListener() {
    private var callStartNanos: Long = 0

    override fun callStart(call: Call) {
        callStartNanos = System.nanoTime()
    }

    override fun callEnd(call: Call) {
        val callEndNanos = System.nanoTime()
        val durationMillis = (callEndNanos - callStartNanos) / 1_000_000
        println("Call ended: ${call.request().url} took ${durationMillis} ms")
    }

    override fun callFailed(call: Call, ioe: IOException) {
        val callEndNanos = System.nanoTime()
        val durationMillis = (callEndNanos - callStartNanos) / 1_000_000
        println("Call failed: ${call.request().url} failed after ${durationMillis} ms with error: $ioe")
    }

    companion object {
        val FACTORY: Factory = object : Factory {
            override fun create(call: Call): EventListener {
                return TimeLoggingEventListener()
            }
        }
    }
}
