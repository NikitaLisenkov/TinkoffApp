package com.example.homework_1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R

class MainActivityHW1 : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var buttonOpenSecondActivity: Button

    private val launcherActivity =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val resultString = data?.getStringExtra(RESULT_EXTRA_KEY)
                textViewResult.text = resultString
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hw1)

        textViewResult = findViewById(R.id.textViewResult)
        buttonOpenSecondActivity = findViewById(R.id.buttonOpenSecondActivity)
        buttonOpenSecondActivity.setOnClickListener {
            val intent = Intent(this@MainActivityHW1, SecondActivityHW1::class.java)
            launcherActivity.launch(intent)
        }
    }

    companion object {
        const val RESULT_EXTRA_KEY = "result"
        const val TAG = "MainActivity"
    }
}