package com.example.intentservice.servicetest

import android.app.IntentService
import android.content.Intent
import android.util.Log

//automatically runs in another thread
class TestIntentService: IntentService("TestIntentService") {

    init {
        instance = this
    }

    override fun onHandleIntent(p0: Intent?) {
        try {
            isRunning = true
            while (isRunning){
                Log.d("TestIntentService", "Service is running")
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException){
            Thread.currentThread().interrupt()
        }
    }

    companion object{

        private lateinit var instance: TestIntentService
        var isRunning = false

        fun stopService(){
            Log.d("TestIntentService", "Service is stopping")
            isRunning = false
            instance.stopSelf()
        }
    }
}