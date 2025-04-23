package com.zzt.zt_okhttp_websocket

import android.app.Application


/**
 * @author: zeting
 * @date: 2025/4/23
 *
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        initHelper();
        initConfig();
    }

    private fun initHelper() {
    }

    private fun initConfig() {
    }
}