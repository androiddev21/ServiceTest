package com.example.intentservice.workmanagertest

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.example.intentservice.databinding.ActivityWorkerTestBinding
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class WorkerTestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWorkerTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkerTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val workManager = WorkManager.getInstance(applicationContext)
        val workRequest = PeriodicWorkRequest.Builder(
            RandomNumberGeneratorWorker::class.java,
            15,
            TimeUnit.MINUTES
        )
            //.setConstraints()
            .build()
        val workRequest1 = OneTimeWorkRequest.Builder(RandomNumberGeneratorWorker::class.java)
            .addTag("worker1")
            .build()
        val workRequest2 = OneTimeWorkRequest.Builder(RandomNumberGeneratorWorker2::class.java)
            .addTag("worker2")
            .build()
        val workRequest3 = OneTimeWorkRequest.Builder(RandomNumberGeneratorWorker3::class.java)
            .addTag("worker3")
            .build()

        val workRequestForeground =
            OneTimeWorkRequest.Builder(RandomNumberGeneratorForegroundWorker::class.java)
                .build()

        binding.bStartService.setOnClickListener {
            //workManager.enqueue(workRequest)

            //will be executed step by step
//            workManager.beginWith(workRequest1)
//                .then(workRequest2)
//                .then(workRequest3)
//                .enqueue()

            //worker1 and worker2 will be executed together, only after then worker3
//            workManager.beginWith(listOf(workRequest1, workRequest2))
//                .then(workRequest3)
//                .enqueue()

            workManager.enqueue(workRequestForeground)
        }
        binding.bStopService.setOnClickListener {
            workManager.cancelWorkById(workRequestForeground.id)
            //if one of workers fails or stops, further will not be executed
            // workManager.cancelAllWorkByTag("worker2")
        }
    }
}