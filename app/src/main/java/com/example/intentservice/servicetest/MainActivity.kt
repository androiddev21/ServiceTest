package com.example.intentservice.servicetest

import android.app.ActivityManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import androidx.core.os.bundleOf
import com.example.intentservice.broadcastreceiver.AirplaneModeChangedReceiver
import com.example.intentservice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var receiver: AirplaneModeChangedReceiver

    private var localBoundService = TestLocalBoundService()
    private var isConnectedToLocalBoundService = false
    private val localServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            val binder = service as TestLocalBoundService.LocalBinder
            localBoundService = binder.getBoundService()
            isConnectedToLocalBoundService = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnectedToLocalBoundService = false
        }
    }

    private lateinit var remoteBoundService: Messenger
    private var isConnectedToRemoteBoundService = false
    private val remoteServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(p0: ComponentName?, service: IBinder?) {
            remoteBoundService = Messenger(service)
            isConnectedToRemoteBoundService = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            isConnectedToRemoteBoundService = false
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //service, intent service
        // testBackgroundService()

        //testForegroundService()

        testBoundService()

        testJobService()

        testImplicitIntent()

        testJobIntentService()
    }

    override fun onStart() {
        super.onStart()
        testReceivers()
    }

    private fun testImplicitIntent() {
        binding.bImplicitIntent.setOnClickListener {
            val implicitIntent = Intent().also {
                it.action = "com.example.intentservice"
                it.addCategory(Intent.CATEGORY_DEFAULT)
            }
            startActivity(implicitIntent)
        }
    }

    private fun testJobService() {
        //job scheduler
        binding.bScheduleJob.setOnClickListener {
            val componentName = ComponentName(this, TestJobService::class.java)
            val info = JobInfo.Builder(123, componentName)
                // .setRequiresCharging(true)
                //when wifi
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                //keep alive after reboot
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build()
            val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            val resultCode = scheduler.schedule(info)
            if (resultCode == JobScheduler.RESULT_SUCCESS) {
                Log.d("MainActivity", "Successfully scheduled")
            } else {
                Log.d("MainActivity", "Failure scheduled")
            }
        }
        binding.bCancelJob.setOnClickListener {
            val scheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
            scheduler.cancel(123)
            Log.d("MainActivity", "Job cancelled")
        }
    }

    private fun testBoundService() {
        //bound service
        //Bound Services is a great way to perform a long running work while you make a smooth responsive UI.
        val intent = Intent(this, TestLocalBoundService::class.java)
        bindService(intent, localServiceConnection, BIND_AUTO_CREATE)
        Toast.makeText(this, localBoundService.getSystemTime(), Toast.LENGTH_SHORT).show()

        val intent1 = Intent(applicationContext, TestRemoteBoundService::class.java)
        bindService(intent1, remoteServiceConnection, BIND_AUTO_CREATE)

        binding.bSendDataToBoundService.setOnClickListener {
            val bundle = bundleOf(
                "MSG_KEY" to "Test message"
            )
            val msg = Message.obtain()
            msg.data = bundle
            try {
                remoteBoundService.send(msg)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun testForegroundService() {
        fun isForegroundServiceRunning(): Boolean {
            val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            manager.getRunningServices(Integer.MAX_VALUE).forEach {
                if (TestForegroundService::class.java.name == it.service.className) {
                    return true
                }
            }
            return false
        }

        if (!isForegroundServiceRunning()) {
            Intent(this, TestForegroundService::class.java).also {
                startForegroundService(it)
            }
        }
    }

    private fun testBackgroundService() {
        binding.bStart.setOnClickListener {
            //            Intent(this, TestIntentService::class.java).also {
            //                startService(it)
            //                binding.tvServiceState.text = "Service running"
            //            }
            Intent(this, TestBackgroundService::class.java).also {
                startService(it)
                binding.tvServiceState.text = "Service running"
            }
        }
        binding.bStop.setOnClickListener {
            //            TestIntentService.stopService()
            //            binding.tvServiceState.text = "Service stopped"
            Intent(this, TestBackgroundService::class.java).also {
                //another way to stop service
                stopService(it)
                binding.tvServiceState.text = "Service stopped"
            }
        }
        binding.bSendData.setOnClickListener {
            //service will not be restarted, onStartCommand will be called
            Intent(this, TestBackgroundService::class.java).also {
                it.putExtra("EXTRA_DATA", "${binding.etDataString.text}")
                startService(it)
            }
        }
    }

    private fun testReceivers() {
        //static receivers - declare in manifest, works only when app is closed
        //for android 8+ should be registered in code
        receiver = AirplaneModeChangedReceiver()
        //example of dynamic receiver
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            //can lead to memory leaks so we need to unregister receiver
            registerReceiver(receiver, it)
        }
    }

    private fun testJobIntentService() {
        binding.bStart.setOnClickListener {
            Intent(this, TestJobIntentService::class.java).also {
                binding.tvServiceState.text = "Service running"
                JobIntentService.enqueueWork(
                    applicationContext,
                    TestJobIntentService::class.java,
                    1,
                    it
                )
            }
        }
        binding.bStop.setOnClickListener {
            //no way to stop such way. It stops only after work is done
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }
}