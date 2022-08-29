package com.example.intentservice.workmanagertest

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import kotlin.random.Random

class RandomNumberGeneratorWorker(val context: Context, val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    private var isRandomGeneratorOn = true

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
        startRandomNumberGenerator()
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        Log.d("Worker1", "Stopped")
    }
}