package com.example.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private val formatter = SimpleDateFormat("d MMM", Locale.US)

    fun convertSecondsToDateStr(seconds: Long): String {
        val calendar = Date(seconds * 1000)
        return formatter.format(calendar)
    }

}