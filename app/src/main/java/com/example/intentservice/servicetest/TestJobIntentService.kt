package com.example.intentservice.servicetest

import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService

class TestJobIntentService : JobIntentService() {

    var counter = 0

    override fun onHandleWork(intent: Intent) {
        try {
            repeat((1..5).count()) {
                if (!isStopped) {
                    Log.d("TestJobIntentService", "Service is running ${counter++}")
                    Thread.sleep(1000)
                }
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
        //need to stop self or stop by platform
        stopSelf()
    }

    override fun onStopCurrentWork(): Boolean {
        counter = 0
        return super.onStopCurrentWork()
    }
}