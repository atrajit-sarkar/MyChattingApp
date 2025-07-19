package com.example.mychattingapp.Utils.DateUtils

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

fun convertToUserTimeZone(
    gmtTime: String,
    inputFormat: String = "yyyy-MM-dd HH:mm:ss",
    outputFormat: String = "yyyy-MM-dd HH:mm:ss"
): String {
    try {
        // Parse the input GMT time
        val gmtDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT") // Set input time zone to GMT
        }
        val date = gmtDateFormat.parse(gmtTime) ?: return "Invalid date"

        // Format the date to the user's local time zone
        val userTimeZoneFormat = SimpleDateFormat(outputFormat, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault() // Set output to user's local time zone
        }
        return userTimeZoneFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return "Error converting time"
    }
}

fun convertToGMT(
    localTime: String,
    inputFormat: String = "yyyy-MM-dd HH:mm:ss",
    outputFormat: String = "yyyy-MM-dd HH:mm:ss"
): String {
    try {
        // Parse the input local time
        val localDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault() // Set input time zone to local
        }
        val date = localDateFormat.parse(localTime) ?: return "Invalid date"

        // Format the date to GMT time zone
        val gmtDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("GMT") // Set output time zone to GMT
        }
        return gmtDateFormat.format(date)
    } catch (e: Exception) {
        e.printStackTrace()
        return "Error converting time"
    }
}
