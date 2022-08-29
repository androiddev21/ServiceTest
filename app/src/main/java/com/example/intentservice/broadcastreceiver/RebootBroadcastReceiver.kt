package com.example.intentservice.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.intentservice.servicetest.TestForegroundService

class RebootBroadcastReceiver: BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
       if(intent?.action == Intent.ACTION_BOOT_COMPLETED){
           Intent(context, TestForegroundService::class.java).also{
               context?.startForegroundService(it)
           }
       }
    }
}