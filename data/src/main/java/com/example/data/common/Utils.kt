package com.example.data.common

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getConvertedDate(date: String): Date? {
        val utcFormat: DateFormat = SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            Locale.getDefault()
        )

        utcFormat.timeZone = TimeZone.getTimeZone("UTC")
        return utcFormat.parse(date)
    }
}