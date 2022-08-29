package com.example.intentservice.servicetest

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.text.SimpleDateFormat
import java.util.*

//runs in same PROCESS
class TestLocalBoundService: Service() {

    private val binder: LocalBinder = LocalBinder()

    override fun onBind(p0: Intent?): IBinder = binder

    fun getSystemTime(): String {
        val simpleDateFormat = SimpleDateFormat("ss:mm:hh dd/mm/yyyy", Locale.getDefault())
        return simpleDateFormat.format(Date())
    }

    inner class LocalBinder: Binder() {

        fun getBoundService() = this@TestLocalBoundService
    }
}