package com.example.testtask

import java.util.Calendar
import java.util.TimeZone

class Utils {
    companion object {
        fun utcMsToSeconds(ms: Long): Long {
            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = ms
            return calendar.get(Calendar.SECOND) * 1L
        }
    }
}