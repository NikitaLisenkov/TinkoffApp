package com.example.homework1.screens

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.homework1.R

class SecondActivity : AppCompatActivity() {

    private lateinit var localBroadcastManager: LocalBroadcastManager

    private val resultReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val resultFromService = intent?.getStringExtra(MainActivity.RESULT_EXTRA_KEY)
            val resultIntent = Intent().putExtra(MainActivity.RESULT_EXTRA_KEY, resultFromService)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        localBroadcastManager = LocalBroadcastManager.getInstance(this)

        val intentFilter = IntentFilter(TimerService.ACTION_RESULT)
        localBroadcastManager.registerReceiver(resultReceiver, intentFilter)

        val serviceIntent = Intent(this, TimerService::class.java)
        startService(serviceIntent)
    }

    override fun onDestroy() {
        super.onDestroy()
        localBroadcastManager.unregisterReceiver(resultReceiver)
    }
}