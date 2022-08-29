package com.example.intentservice.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AirplaneModeChangedReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
       val isAirplaneModeEnabled = intent?.getBooleanExtra("state", false) ?: return
        val text = if (isAirplaneModeEnabled) "Airplane mode enabled" else "Airplane mode disabled"
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}