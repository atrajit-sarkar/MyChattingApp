package com.example.mychattingapp.Utils.DateUtils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatDateForDisplay(date: String): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val parsedDate = sdf.parse(date) ?: return date

    val today = Calendar.getInstance()
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }
    val messageCalendar = Calendar.getInstance().apply { time = parsedDate }

    return when {
        today.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR) &&
                today.get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR) -> {
            "Today"
        }
        yesterday.get(Calendar.YEAR) == messageCalendar.get(Calendar.YEAR) &&
                yesterday.get(Calendar.DAY_OF_YEAR) == messageCalendar.get(Calendar.DAY_OF_YEAR) -> {
            "Yesterday"
        }
        else -> SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(parsedDate)
    }
}
