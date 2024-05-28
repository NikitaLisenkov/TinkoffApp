package com.example.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    val sdfDayMonth = SimpleDateFormat("d MMM", Locale.US)
    val sdfHourMinute = SimpleDateFormat("HH:mm", Locale.US)

    fun convertSecondsToDateStr(seconds: Long, formatter: SimpleDateFormat = sdfDayMonth): String {
        val calendar = Date(seconds * 1000)
        return formatter.format(calendar)
    }

}