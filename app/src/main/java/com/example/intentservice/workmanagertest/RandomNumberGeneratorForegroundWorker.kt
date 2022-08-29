package com.example.intentservice.workmanagertest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.ForegroundInfo
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.intentservice.R
import kotlin.random.Random

private const val NOTIFICATION_ID = 10

class RandomNumberGeneratorForegroundWorker(
    val context: Context,
    val workerParameters: WorkerParameters
) :
    Worker(context, workerParameters) {

    private fun startRandomNumberGenerator() {
        repeat((0 until 5).count()) {
            if (!isStopped) {
                try {
                    val randomNumber = Random.nextInt(1, 1000)
                    Log.d("Worker1", "Random number is $randomNumber")
                    Thread.sleep(1000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun doWork(): Result {
        setForegroundAsync(createForegroundInfo())
        startRandomNumberGenerator()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d("Worker1", "Stopped")
    }

    private fun createForegroundInfo(): ForegroundInfo {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "FOREGROUND_SERVICE_ID",
                "FOREGROUND_SERVICE_ID",
                NotificationManager.IMPORTANCE_LOW
            )
            context.getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }
        val notification = NotificationCompat.Builder(context, "FOREGROUND_SERVICE_ID")
            .setContentText("Worker service is running")
            .setContentTitle("Worker service is enabled")
            .setSmallIcon(R.drawable.ic_stat_name)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }
}