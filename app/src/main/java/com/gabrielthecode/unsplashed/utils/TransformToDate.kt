package com.gabrielthecode.unsplashed.utils

import java.text.SimpleDateFormat
import java.util.TimeZone

fun String.transformToDate(): String? {
    return try {
        // Input format
        val inputFormat = SimpleDateFormat("yy-MM-dd'T'HH:mm:ssXXX")
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Assuming input is in UTC

        // Output format
        val outputFormat = SimpleDateFormat("dd/MM/YYYY, HH:mm")
        outputFormat.timeZone = TimeZone.getDefault() // Use the local time zone

        val date = inputFormat.parse(this)
        outputFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}