package com.example.homework1.screens

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.Main)

    private val localBroadcastManager by lazy {
        LocalBroadcastManager.getInstance(this)
    }

    override fun onCreate() {
        super.onCreate()
        log("onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand")
        serviceScope.launch {
            for (i in 0 until 11) {
                delay(1000)
                log("Timer $i")
            }
            val result = "timer is over (10 sec)"
            val resultIntent = Intent(ACTION_RESULT).putExtra(MainActivityHW1.RESULT_EXTRA_KEY, result)
            localBroadcastManager.sendBroadcast(resultIntent)
        }
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
        log("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun log(message: String) {
        Log.d("SERVICE_TAG", "TimerService: $message")
    }

    companion object {
        const val ACTION_RESULT = "action_result"
    }
}
