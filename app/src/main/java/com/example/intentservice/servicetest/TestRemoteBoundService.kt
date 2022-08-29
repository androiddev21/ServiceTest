package com.example.intentservice.servicetest

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.widget.Toast

//runs in another PROCESS
class TestRemoteBoundService: Service() {

    val messenger = Messenger(ReceiveMessageHandler())

    override fun onBind(p0: Intent?): IBinder? = messenger.binder

    inner class ReceiveMessageHandler: Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val bundle = msg.data
            val receivedData = bundle.getString("MSG_KEY")
            Toast.makeText(applicationContext, receivedData, Toast.LENGTH_LONG).show()
        }
    }
}