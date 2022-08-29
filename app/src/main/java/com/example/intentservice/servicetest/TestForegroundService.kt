package com.example.intentservice.servicetest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.intentservice.R
import kotlin.concurrent.thread

class TestForegroundService : Service() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        thread {
            while (true) {
                Log.d("Foreground service", "Service is running")
                try {
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        val channel = NotificationChannel(
            "FOREGROUND_SERVICE_ID",
            "FOREGROUND_SERVICE_ID",
            NotificationManager.IMPORTANCE_LOW
        )
        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
        val notification = Notification.Builder(this, "FOREGROUND_SERVICE_ID")
            .setContentText("Service is running")
            .setContentTitle("Service is enabled")
            .setSmallIcon(R.drawable.ic_launcher_background)
        startForeground(1001, notification.build())
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}