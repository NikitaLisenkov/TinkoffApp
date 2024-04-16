package com.example.app.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {

    private val formatter = SimpleDateFormat("d MMM", Locale.getDefault())

    fun convertMillisToDateStr(millis: Long): String {
        val calendar = Date(millis)
        return formatter.format(calendar)
    }

}