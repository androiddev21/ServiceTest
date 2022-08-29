package com.example.intentservice.servicetest

import android.app.job.JobParameters
import android.app.job.JobService
import android.util.Log
import kotlin.concurrent.thread

//by default runs on UI thread
class TestJobService : JobService() {

    private var isJobCancelled = false

    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d("JobService", "Job started")
        doBackgroundWork(params)
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        Log.d("JobService", "Job cancelled before completion")
        isJobCancelled = true
        return true
    }

    private fun doBackgroundWork(params: JobParameters?) {
        thread {
            (1..10).forEach {
                if (isJobCancelled) {
                    return@thread
                }
                Log.d("JobService", "run $it")
                Thread.sleep(1000)
            }
            Log.d("JobService", "Job finished")
            //to reschedule set true
            jobFinished(params, false)
        }
    }
}