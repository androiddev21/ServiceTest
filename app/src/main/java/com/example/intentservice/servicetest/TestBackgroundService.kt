package com.example.intentservice.servicetest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

//by default runs in the same thread
class TestBackgroundService : Service() {

    val TAG = "TestService"

    init {
        Log.d(TAG, "Service is running")
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString = intent?.getStringExtra("EXTRA_DATA")
        dataString?.let {
            Log.d(TAG, dataString)
        }
        Thread {
            while (true) {

            }
        }.start()
        //how system handles if kills service
        //START_NOT_STICKY - if system kills it will not be recreated
        //START_STICKY - service will be recreated when possible and the last intent will not be redelivered
        //START_REDELIVER_INTENT - service will be recreated when possible and the last intent will be redelivered
        return START_STICKY
    }
}